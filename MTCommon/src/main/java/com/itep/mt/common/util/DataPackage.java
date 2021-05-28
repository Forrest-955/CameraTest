package com.itep.mt.common.util;

import com.itep.mt.common.constant.version.GenericConstant;
import com.itep.mt.common.sys.SMAction;
import com.itep.mt.common.sys.SysConf;

/**
 * 0203数据包格式处理
 */

public class DataPackage {
    static DataStore dataStore = new DataStore();
    /**
     * 检查数据包的完整性
     *
     * @param data 数据包数据
     * @return true表示完整，false表示有错
     */
    public static boolean checkPkg(byte[] data) {
        if (data.length < 5) {//至少5个字节，2个字节参数长度，2个字节命令码，1个字节校验码
            return false;
        }

        //判断参数长度是否有效

        int len = StringUtil.bytesToInt(data[0]) * 256 + StringUtil.bytesToInt(data[1]);
        int datalen = data.length - 3;
        if (len != datalen) {
            return false;
        }

        byte crc = 0;
        for (int i = 0; i < datalen; i++) {
            crc ^= data[2 + i];
        }
        if (crc != data[data.length - 1]) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 组合生成包（格式为0x02 数据长度 命令码或状态码 参数 校验值 0x03）
     *
     * @param cmdh  命令码或状态码第1个字节
     * @param cmdl  命令码或状态码第2个字节
     * @param param 参数，如果没有，可传入null
     * @return 完整的包
     */
    public static byte[] buildPkg(byte cmdh, byte cmdl, byte[] param) {
        return buildPkg(cmdh, cmdl, param, 0, param != null ? param.length : 0);
    }

    /**
     * 组合生成包（格式为0x02 数据长度 命令码或状态码 参数 校验值 0x03）
     *
     * @param cmdh         命令码或状态码第1个字节
     * @param cmdl         命令码或状态码第2个字节
     * @param param        参数，如果没有，可传入null
     * @param param_offset 参数在param数组中的偏移量
     * @param param_len    参数长度
     * @return 完整的包
     */
    public static byte[] buildPkg(byte cmdh, byte cmdl, byte[] param, int param_offset, int param_len) {
        //计算长度
        int paramlen = param == null ? 0 : param_len;
        int size = 2 + (5 + paramlen) * 2;

        byte[] pkg = new byte[size];
        pkg[0] = 0x02;
        pkg[size - 1] = 0x03;

        //组装转拆分的数据
        int datalen = 2 + paramlen;
        pkg[1] = (byte) (datalen >> 8);
        pkg[2] = (byte) (datalen & 0xFF);
        pkg[3] = cmdh;
        pkg[4] = cmdl;
        if (param != null) {
            System.arraycopy(param, param_offset, pkg, 5, paramlen);
        }

        //计算校验值
        byte crc = 0;
        for (int i = 0; i < datalen; i++) {
            crc ^= pkg[3 + i];
        }
        pkg[1 + 2 + datalen] = crc;

        byte[] ascdata = StringUtil.bytesToAsc(pkg, 1, 5 + paramlen);
        System.arraycopy(ascdata, 0, pkg, 1, ascdata.length);
        return pkg;
    }

    /**
     * 广发版本回复（老207回复）
     * 组合生成包（格式为0x02 数据长度  参数 校验值 0x03）
     *
     * @param param        参数，如果没有，可传入null
     * @param param_offset 参数在param数组中的偏移量
     * @param param_len    参数长度
     * @return 完整的包
     */
    public static byte[] buildPkg(byte[] param, int param_offset, int param_len) {
        //计算长度
        int paramlen = param == null ? 0 : param_len;
        int size = 2 + 6 + paramlen * 2;
        Logger.i("size:" + size);

        byte[] pkg = new byte[size];
        pkg[0] = 0x02;
        pkg[size - 1] = 0x03;

        //组装转拆分的数据
        int datalen = paramlen;
        pkg[2] = (byte) (datalen >> 8);
        pkg[1] = (byte) (datalen & 0xFF);
        if (param != null) {
            System.arraycopy(param, param_offset, pkg, 3, paramlen);
        }

        //计算校验值
        byte crc = 0;
        for (int i = 0; i < datalen; i++) {
            crc ^= pkg[3 + i];
        }
        pkg[1 + 2 + datalen] = crc;

        //组包成02打头03结尾，中间部分拆分
        byte[] ascdata = StringUtil.bytesToAsc(pkg, 1, 3 + paramlen);
        System.arraycopy(ascdata, 0, pkg, 1, ascdata.length);
        Logger.i("pkg.length:" + pkg.length);
        if ("平顶山银行".equals(SysConf.getAppVersionName())||"河南农信".equals(SysConf.getAppVersionName())){//不需要组成每60前面加00020200
            return pkg;
        }
        //组成每60前面加00020200
        int add = pkg.length / 60;//整数部分
        Logger.i("整数部分:" + add);
        int remainder = pkg.length % 60;//余数
        Logger.i("余数:" + remainder);
        byte[] ret = new byte[pkg.length + 4 * (add + 1)];//需要返回的长度
        for (int i = 0; i <= add; i++) {
            if (i == 0) {
                ret[0] = 0x00;
                ret[1] = 0x02;
                ret[2] = 0x02;
                ret[3] = 0x00;
                if (i == add) {
                    if (remainder==0){//如果余数为0，则表示只有60倍数的长度数据，余数部分就不处理
                        break;
                    }
                    System.arraycopy(pkg, 0, ret, 4, remainder);
                } else {
                    System.arraycopy(pkg, 0, ret, 4, 60);
                }
            } else {
                ret[i * 64 + 0] = 0x00;
                ret[i * 64 + 1] = 0x02;
                ret[i * 64 + 2] = 0x02;
                ret[i * 64 + 3] = 0x00;
                if (i == add) {
                    if (remainder==0){//如果余数为0，则表示只有60倍数的长度数据，余数部分就不处理
                        break;
                    }
                    System.arraycopy(pkg, 60 * add, ret, add * 64 + 4, remainder);
                } else {
                    System.arraycopy(pkg, 60 * i, ret, i * 64 + 4, 60);
                }
            }
        }

        return ret;
    }

    /**
     * 组合生成包（格式为0x02 数据长度 命令码或状态码 参数 校验值 0x03）
     *
     * @param cmdh         命令码或状态码第1个字节
     * @param cmdl         命令码或状态码第2个字节
     * @param param        参数，如果没有，可传入null
     * @param param_offset 参数在param数组中的偏移量
     * @param param_len    参数长度
     * @return 完整的包
     */
    public static byte[] buildPkgGXNX(byte cmdh, byte cmdl, byte[] param, int param_offset, int param_len) {
        //计算长度
        int paramlen = param == null ? 0 : param_len;
        int size = 2 + (5 + paramlen) * 2;

        byte[] pkg = new byte[size];
        pkg[0] = 0x02;
        pkg[size - 1] = 0x03;

        //组装转拆分的数据
        int datalen = 2 + paramlen;
        pkg[2] = (byte) (datalen >> 8);
        pkg[1] = (byte) (datalen & 0xFF);
        pkg[3] = cmdh;
        pkg[4] = cmdl;
        if (param != null) {
            System.arraycopy(param, param_offset, pkg, 5, paramlen);
        }

        //计算校验值
        byte crc = 0;
        for (int i = 0; i < datalen; i++) {
            crc ^= pkg[3 + i];
        }
        pkg[1 + 2 + datalen] = crc;

        byte[] ascdata = StringUtil.bytesToAsc(pkg, 1, 5 + paramlen);
        System.arraycopy(ascdata, 0, pkg, 1, ascdata.length);
        return pkg;
    }

    /**
     * 组合生成包（格式为0x04 数据长度 命令码或状态码 参数 校验值  0x05）
     *
     * @param cmdh         命令码或状态码第1个字节
     * @param cmdl         命令码或状态码第2个字节
     * @param param        参数，如果没有，可传入null
     * @param param_offset 参数在param数组中的偏移量
     * @param param_len    参数长度
     * @return 完整的包
     */
    public static byte[] gmPkg(byte cmdh, byte cmdl, byte[] param, int param_offset, int param_len) {
        //计算长度
        int paramlen = param == null ? 0 : param.length - param_offset;
        Logger.e("paramlen:" + paramlen + "offset:" + param_offset);
        int content_len = 8 + 4 + paramlen * 2 + 32;//dn+cmd+data+hash
        int fixLength = content_len % 32;
        int size = fixLength == 0 ? 2 + 4 + content_len : 2 + 4 + content_len + 32 - fixLength;
        byte[] pkg = new byte[size];
        pkg[0] = 0x04;
        pkg[size - 1] = 0x05;

        //data的长度，不包含DN和MAC,包含CMD，单位：字节
        int datalen = paramlen + 2;
        pkg[1] = (byte) (datalen >> 8);
        pkg[2] = (byte) (datalen & 0xFF);

        String originDataStr = "";
        //Dn+1

        int localDn = StringUtil.bytesToInt(StringUtil.hexToBytes(dataStore.getString(GenericConstant.GM_DEVICE_NUM)));
        Logger.e("返回dn:" + localDn + "+1");
        byte[] dn = StringUtil.intToBytes2(localDn + 1);
        String dnStr = StringUtil.bytesToHex(dn);
        dataStore.putString(GenericConstant.GM_DEVICE_NUM, dnStr);
        originDataStr += dnStr;

        originDataStr += StringUtil.bytesToHex(new byte[]{cmdh, cmdl});
        if (paramlen != 0) {
            originDataStr += StringUtil.bytesToHex(param).substring(param_offset * 2);
        }


        String macKey = null;
        try {
            macKey = SysConf.getGMMacKey();//mac密钥
        } catch (Exception e) {
            e.printStackTrace();
            macKey = GenericConstant.GM_MACKEYDEFAULT;
        }
        Logger.i("mackey:" + macKey);
        originDataStr += SMAction.HMACSM3(macKey, originDataStr).substring(0, 32);

        String transKey = null;
        try {
            transKey = SysConf.getGMTramsKey();//传输密钥
        } catch (Exception e) {
            e.printStackTrace();
            transKey = GenericConstant.GM_TRAMSKEYDEFAULT;
        }
        if (fixLength != 0) {
            StringBuilder zeroPatch = new StringBuilder();
            for (int i = 0; i < 32 - fixLength; i++) {
                zeroPatch.append("0");
            }
            originDataStr += zeroPatch.toString();
        }
        Logger.e("reply:" + originDataStr);
        String encryptDataStr = SMAction.sm4_Encrypt(originDataStr, originDataStr.length() / 2, transKey, transKey.length() / 2);
        Logger.e("encrypt:" + encryptDataStr + "length:" + encryptDataStr.length());
        byte[] encryptDataByte = StringUtil.hexToBytes(encryptDataStr);
        byte[] ascDatalen = StringUtil.bytesToAsc(pkg, 1, 2);
        byte[] ascdata = StringUtil.bytesToAsc(encryptDataByte, 0, encryptDataByte.length);
        System.arraycopy(ascDatalen, 0, pkg, 1, ascDatalen.length);
        System.arraycopy(ascdata, 0, pkg, 5, ascdata.length);
        Logger.e("reply:" + StringUtil.bytesToHex(pkg) + "length:" + pkg.length);
        return pkg;
    }

    /**
     * 组合生成包（格式为0x04 数据长度 命令码或状态码 参数 校验值 0x05）
     *
     * @param cmdh         命令码或状态码第1个字节
     * @param cmdl         命令码或状态码第2个字节
     * @param param        参数，如果没有，可传入null
     * @param param_offset 参数在param数组中的偏移量
     * @param param_len    参数长度
     * @return 完整的包
     */
    public static byte[] OTKPkg(byte cmdh, byte cmdl, byte[] param, int param_offset, int param_len,String transKey) {
        //计算长度
        int paramlen = param == null ? 0 : param.length - param_offset;
        Logger.e("paramlen:" + paramlen + "offset:" + param_offset);
        int content_len = 8 + 4 + paramlen * 2 + 32;//dn+cmd+data+hash
        int fixLength = content_len % 32;
        int size = fixLength == 0 ? 2 + 4 + content_len : 2 + 4 + content_len + 32 - fixLength;
        byte[] pkg = new byte[size];
        pkg[0] = 0x04;
        pkg[size - 1] = 0x05;

        //data的长度，不包含DN和MAC,包含CMD，单位：字节
        int datalen = paramlen + 2;
        pkg[1] = (byte) (datalen >> 8);
        pkg[2] = (byte) (datalen & 0xFF);

        String originDataStr = "";
        //Dn+1

        int localDn = StringUtil.bytesToInt(StringUtil.hexToBytes(dataStore.getString(GenericConstant.GM_DEVICE_NUM)));
        Logger.e("返回dn:" + localDn + "+1");
        byte[] dn = StringUtil.intToBytes2(localDn + 1);
        String dnStr = StringUtil.bytesToHex(dn);
        dataStore.putString(GenericConstant.GM_DEVICE_NUM, dnStr);
        originDataStr += dnStr;

        originDataStr += StringUtil.bytesToHex(new byte[]{cmdh, cmdl});
        if (paramlen != 0) {
            originDataStr += StringUtil.bytesToHex(param).substring(param_offset * 2);
        }


        String macKey = null;
        try {
            macKey = SysConf.getGMMacKey();//mac密钥
        } catch (Exception e) {
            e.printStackTrace();
            macKey = GenericConstant.GM_MACKEYDEFAULT;
        }
        originDataStr += SMAction.HMACSM3(macKey, originDataStr).substring(0, 32);


        if (fixLength != 0) {
            StringBuilder zeroPatch = new StringBuilder();
            for (int i = 0; i < 32 - fixLength; i++) {
                zeroPatch.append("0");
            }
            originDataStr += zeroPatch.toString();
        }
        Logger.e("reply:" + originDataStr);
        String encryptDataStr = SMAction.sm4_Encrypt(originDataStr, originDataStr.length() / 2, transKey, transKey.length() / 2);
        Logger.e("encrypt:" + encryptDataStr + "length:" + encryptDataStr.length());
        byte[] encryptDataByte = StringUtil.hexToBytes(encryptDataStr);
        byte[] ascDatalen = StringUtil.bytesToAsc(pkg, 1, 2);
        byte[] ascdata = StringUtil.bytesToAsc(encryptDataByte, 0, encryptDataByte.length);
        System.arraycopy(ascDatalen, 0, pkg, 1, ascDatalen.length);
        System.arraycopy(ascdata, 0, pkg, 5, ascdata.length);
        Logger.e("reply:" + StringUtil.bytesToHex(pkg) + "length:" + pkg.length);
        return pkg;
    }
}
