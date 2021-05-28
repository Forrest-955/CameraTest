package com.itep.mt.common.sys;

import com.itep.mt.common.util.Logger;
import com.itep.mt.common.util.PokerTest;
import com.itep.mt.common.util.StringUtil;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/21.
 */

public class SMAction {
    private static long DEFULT_TIMEOUT = 30000;//30秒超时

    /**
     * 生成SM2公私钥对
     *
     * @return 是否成功
     */
    public static boolean sm2_CreateKeys() {
        try {
            JSONObject params = new JSONObject();
            CmdResonse cr = SysAccessor.sendOneCmd("sm2_create_keys", params, DEFULT_TIMEOUT);
            if (!cr.getResult()) {
                Logger.e(cr.getErr_msgs());
            }
            return cr.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 导入公私钥对
     *
     * @param publickey  公钥信息
     * @param privatekey 私钥信息
     * @return 导入是否成功
     */
    public static boolean sm2_InputKeys(String publickey, String privatekey) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("publickey", publickey);
            params.put("privatekey", privatekey);
            CmdResonse cr = SysAccessor.sendOneCmd("sm2_input_keys", params, DEFULT_TIMEOUT);
            if (!cr.getResult()) {
                Logger.e(cr.getErr_msgs());
            }
            return cr.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取公私钥对
     *
     * @return 公钥信息
     */
    public static JSONObject sm2_GetKeyPair() {
        try {
            JSONObject params = new JSONObject();
            CmdResonse cr = SysAccessor.sendOneCmd("sm2_get_keys", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                return jsdata;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取公钥
     *
     * @return 公钥信息
     */
    public static String sm2_GetPublicKey() {
        try {
            JSONObject params = new JSONObject();
            CmdResonse cr = SysAccessor.sendOneCmd("sm2_get_keys", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String publickeys = jsdata.optString("publickey");
                Logger.i("publickeys:" + publickeys);
                return publickeys;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SM2加密
     *
     * @param input 需要加密的数据
     * @param nlen  数据的长度
     * @return 加密后的数据
     */
    public static String sm2_Encrypt(String input, int nlen) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("input", input);
            params.put("intput_length", nlen);

            CmdResonse cr = SysAccessor.sendOneCmd("sm2_encrypt", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("encrypt_data");
                int len = jsdata.optInt("encrypt_len");   //底层加解密数据 实际长度
                return data;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SM2加密
     *
     * @param input 需要加密的数据
     * @param nlen
     * @param type  为true则表示按C1,C2,C3排列，为false则不处理，默认为C1,C3,C2
     * @return 加密后的数据数据的长度
     */
    public static String sm2_Encrypt(String input, int nlen, boolean type) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("input", input);
            params.put("intput_length", nlen);

            CmdResonse cr = SysAccessor.sendOneCmd("sm2_encrypt", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("encrypt_data");
                Logger.i("data:" + data);
                int len = jsdata.optInt("encrypt_len");   //底层加解密数据 实际长度
                if (type) {//如果为true，则拼成C1+C2+C3返回
                    String C1Str = data.substring(0, 128);
                    String C2Str = data.substring(128 + 64, data.length());
                    String C3Str = data.substring(128, 128 + 64);
                    Logger.i("C1Str:" + C1Str);
                    Logger.i("C2Str:" + C2Str);
                    Logger.i("C3Str:" + C3Str);
                    return C1Str + C2Str + C3Str;
                } else {
                    return data;
                }
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SM2解密
     *
     * @param input 需要解密的数据
     * @param nlen  数据的长度
     * @return 解密后的数据
     */
    public static String sm2_Decrypt(String input, int nlen) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("input", input);
            params.put("intput_length", nlen);

            CmdResonse cr = SysAccessor.sendOneCmd("sm2_decrypt", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("decrypt_data");
                int len = jsdata.optInt("decrypt_len");  //底层加解密数据 实际长度
                return data;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SM3 杂凑值计算  数据
     *
     * @param input 源数据
     * @param nlen  数据的长度
     * @return SM3杂凑值
     */
    public static String sm3_Hash(String input, int nlen) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("input", input);
            params.put("intput_length", nlen);

            CmdResonse cr = SysAccessor.sendOneCmd("sm3_hash", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("hash_data");
                return data;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * SM3 杂凑值计算  文件
     *
     * @param input 源数据
     * @param nlen  数据的长度
     * @return SM3杂凑值
     */
    public static String sm3_Hash_file(String input, int nlen) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("file", input);

            CmdResonse cr = SysAccessor.sendOneCmd("sm3_hash_file", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("hash_data");
                return data;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * SM3计算文件杂凑值
     *
     * @param file_path 文件全路径名
     * @return SM3杂凑值
     */
    public static String sm3_Hash_file(String file_path) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("file", file_path);

            CmdResonse cr = SysAccessor.sendOneCmd("sm3_hash_file", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("hash_data");
                return data;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * HMAC SM3
     *
     * @param keyHexstring   :密钥
     * @param inputHexstring :输入的数据
     * @return :hmacSM3的结果
     */
    public static String HMACSM3(String keyHexstring, String inputHexstring) {

        byte[] ipad = new byte[64];
        byte[] opad = new byte[64];

        for (int i = 0; i < 64; i++) {
            ipad[i] = (byte) 0x36;
            opad[i] = (byte) 0x5C;
        }
        int keyLen = keyHexstring.length() / 2;
        if (keyLen > 64) {
            keyHexstring = sm3_Hash(keyHexstring, keyHexstring.length() / 2);
            keyLen = 32;
        }
        byte[] keybytes = StringUtil.hexToBytes(keyHexstring);
        for (int i = 0; i < keyLen; i++) {
            ipad[i] = (byte) (0x36 ^ keybytes[i]);
            opad[i] = (byte) (0x5C ^ keybytes[i]);
        }
        String ipadString = StringUtil.bytesToHex(ipad);
        String opadString = StringUtil.bytesToHex(opad);
        String tmp = ipadString + inputHexstring;
        String tmpHash = sm3_Hash(tmp, tmp.length() / 2);
        tmp = opadString + tmpHash;
        return sm3_Hash(tmp, tmp.length() / 2);
    }

    /**
     * SM4 加密
     *
     * @param input_data 需要加密的数据
     * @param inputlen   数据长度
     * @param input_key  工作密钥
     * @param keylen     工作密钥长度
     * @return
     */
    public static String sm4_Encrypt(String input_data, int inputlen, String input_key, int keylen) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("input_data", input_data);
            params.put("intput_data_length", inputlen);
            params.put("input_key", input_key);
            params.put("intput_key_length", keylen);

            CmdResonse cr = SysAccessor.sendOneCmd("sm4_encrypt_ebc", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("encrypt_data");
                int len = jsdata.optInt("encrypt_len");  //底层加解密数据 实际长度
                return data;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SM4 解密
     *
     * @param input_data 需要解密的数据
     * @param inputlen   数据长度
     * @param input_key  工作密钥
     * @param keylen     工作密钥长度
     * @return
     */
    public static String sm4_Decrypt(String input_data, int inputlen, String input_key, int keylen) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("input_data", input_data);
            params.put("intput_data_length", inputlen);
            params.put("input_key", input_key);
            params.put("intput_key_length", keylen);

            CmdResonse cr = SysAccessor.sendOneCmd("sm4_decrypt_ebc", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("decrypt_data");
                int len = jsdata.optInt("decrypt_len");  //底层加解密数据 实际长度
                return data;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /******************************国 密*******************************/
    private final static String puKey = "A71236D42EC3434C071B6D3B69403EE5A4A66D623CF9FFB9B7D143854880ABA87F8205485D0034A1B45F699071E0A27B5BA1716E92475B425D7A3EC8A92CC92E";//公钥
    private final static String priKey = "90A714ECCA1BE252F23769B36E89DF7D1A153C5F9F907B673F625DC2EEE45EE4";//私钥
    private final static String plainText = "12345678123456781234567812345678";//明文数据
    private final static String cipherText = "37EE6F65C09C54F7EC27DAE7A621AA89FF455FC02A4474A622B28078223654FFF692412C1C11690F616381EF65E288458F05EACC5E742DBF8201C8E1D3026563B5D07F7B94F668DD828D3D6FFF71B93380D5B33D144F1801AE4F088968FD40D443F95085C92201BADACC1DFEFF1884A8";//密文数据
    private final static String cipherSignText = "4C098E0462B4B1A7817BA8AA1691FDF8F0F9AFEF5E1F4BBF9799ACA8A07DDEED338BD8F15E7B1A73368B59768AE07226BF12D26C23C68404A7CAC55EC53FDDD3";
    private final static String sm4Key = "38383838383838383838383838383838";//

    /**
     * sm2算法自检
     *
     * @return
     */
    public static boolean sm2Check() {
        if (!sm2_InputKeys(puKey, priKey)) {//置入固定密钥
            Logger.i("SMAction sm2Check 置入固定密钥失败");
            return false;
        }
        String plainText = sm2_Decrypt(cipherText, cipherText.length());//对固定密钥进行解密
        Logger.i("SMAction sm2Check plainText:" + plainText);
        if (SMAction.plainText.equals(plainText)) {
            //对明文进行加密在进行解密
            String cText = sm2_Encrypt(SMAction.plainText, SMAction.plainText.length());
            Logger.i("SMAction sm2Check cText:" + cText);
            String decryptText = sm2_Decrypt(cText, cText.length());
            if (SMAction.plainText.equals(decryptText)) {
                Logger.i("SMAction sm2Check 自检成功:");
                return true;
            }
        }
        Logger.i("SMAction sm2Check 自检失败:");
        return false;
    }

    /**
     * sm2电子签名验签自检
     *
     * @return
     */
    public static boolean sm2SignCheck() {
        if (!sm2_InputKeys(puKey, priKey)) {//置入固定密钥
            Logger.i("SMAction sm2SignCheck 置入固定密钥失败");
            return false;
        }
        if (!sm2_verity_sign(plainText, cipherSignText)) {
            Logger.i("SMAction sm2SignCheck 数字签名验签失败");
        } else {
            JSONObject jsdata = sm2_sign(plainText);
            if (jsdata != null) {
                String signatrue_data = jsdata.optString("signatrue_data");
                Logger.i("SMAction sm2SignCheck signatrue_data:" + signatrue_data);
                if (sm2_verity_sign(plainText, signatrue_data)) {
                    Logger.i("SMAcion sm2SignCheck 数字签名自检成功");
                    return true;
                }
            }
        }
        Logger.i("SMAction sm2SignCheck 数字签名自检失败");
        return false;
    }

    /**
     * SM2签名数据
     *
     * @param input 需要签名的数据
     * @return json数据
     */
    public static JSONObject sm2_sign(String input) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("content_data", input);
            CmdResonse cr = SysAccessor.sendOneCmd("sm2_create_sign", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                return jsdata;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SM2验证签名
     *
     * @param input 需要解密的数据
     * @return json数据
     */
    public static boolean sm2_verity_sign(String input, String signatrue_data) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("content_data", input);
            params.put("signatrue_data", signatrue_data);

            CmdResonse cr = SysAccessor.sendOneCmd("sm2_verity_sign", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {

                return true;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * sm3自检
     *
     * @return
     */
    public static boolean sm3Check() {
        String text = "1234567812345678";
        String sm3Hash = "80E52C2A98CB67BA36EB5B00C65E938454E814E8EA0A41CA54D6027E5F553EDD";//固定杂凑值
        String sm3HashValue = sm3_Hash(text, text.length());
        if (sm3Hash.equals(sm3HashValue)) {
            Logger.i("SMAction sm3Check：sm3自检成功");
            return true;
        }
        Logger.i("SMAction sm3Check：sm3自检失败");
        return false;
    }

    /**
     * sm4ecb自检
     *
     * @return
     */
    public static boolean sm4ECBCheck() {
        String iv = "00000000000000000000000000000000";
        String text = "30313233343536373031323334353637";//明文
        String enText = "839CCE7C51DE18706ED1C6A97A89F874";//密文
        if (enText.equals(sm4_Encrypt(text, text.length(), sm4Key, sm4Key.length())) && text.equals(sm4_Decrypt(enText, enText.length(), sm4Key, sm4Key.length()))) {
            Logger.i("SMAction sm4ECBCheck:自检成功");
            return true;
        }
        return false;
    }

    /**
     * sm4cbc自检
     *
     * @return
     */
    public static boolean sm4CBCCheck() {
        String iv = "1A0DA1F91E41C6C9031FDE6D3A871584";
        String text = "1354BE7B30E977B14B0688B17AED391C3106F5FAB10582758C8C484AE5E8A64C";//明文
        String enText = "58618698704A076FB7BC1416BE6AA8DAD85850D2EDBA46A88F5FC41868AA725F";//密文
        if (enText.equals(sm4_Encryp_cbc(text, text.length(), sm4Key, sm4Key.length(), iv)) && text.equals(sm4_Decrypt_cbc(enText, enText.length(), sm4Key, sm4Key.length(), iv))) {
            Logger.i("SMAction sm4CBCCheck:自检成功");
            return true;
        }
        return false;
    }

    /**
     * SM4 加密   CBC
     *
     * @param input_data 需要加密的数据
     * @param inputlen   数据长度
     * @param input_key  工作密钥
     * @param keylen     工作密钥长度
     * @return
     */
    public static String sm4_Encryp_cbc(String input_data, int inputlen, String input_key, int keylen, String input_iv) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("input_data", input_data);
            params.put("intput_data_length", inputlen);
            params.put("input_key", input_key);
            params.put("intput_key_length", keylen);
            params.put("input_IV", input_iv);

            CmdResonse cr = SysAccessor.sendOneCmd("sm4_encrypt_cbc", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("encrypt_data");
                int len = jsdata.optInt("encrypt_len");  //底层加解密数据 实际长度
                return data;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SM4 解密  CBC
     *
     * @param input_data 需要解密的数据
     * @param inputlen   数据长度
     * @param input_key  工作密钥
     * @param keylen     工作密钥长度
     * @return
     */
    public static String sm4_Decrypt_cbc(String input_data, int inputlen, String input_key, int keylen, String input_iv) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("input_data", input_data);
            params.put("intput_data_length", inputlen);
            params.put("input_key", input_key);
            params.put("input_IV", input_iv);
            params.put("intput_key_length", keylen);

            CmdResonse cr = SysAccessor.sendOneCmd("sm4_decrypt_cbc", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("decrypt_data");
                int len = jsdata.optInt("decrypt_len");  //底层加解密数据 实际长度
                return data;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean sm4PinBlockCheck() {
        //todo
        //128位=16字节=32Byte
        //国密pinblock规则
        //明文pin字段：
        //0NPPPP最多12位密码，后补F，N为密码长度
        //主账号字段
        //0*12位+账号，主账号默认全为0
        return true;
    }

    /**
     * 生成随机数，单次最大支持128字节
     *
     * @param random_len 要生成的密钥长度,字节
     * @return
     */
    public static String randomNum_get(int random_len) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("random_len", random_len);
            CmdResonse cr = SysAccessor.sendOneCmd("RandomNum_get", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();

                String data = jsdata.optString("random_data");
                Logger.i("random_data:" + data);
                return data;
            } else {
                Logger.i("randomNum_get:");
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String randomNum_getChecked(int length) {
        String randomStr = randomNum_get(length);
        if (PokerTest.randomTest(randomStr, 2)) {
            return randomStr;
        } else {
            randomStr = randomNum_get(length);
            if (PokerTest.randomTest(randomStr, 2)) {
                return randomStr;
            } else {
                Logger.e("getRandomFailed");
                return "";
            }
        }

    }


    /**
     * 扑克算法检测随机数
     *
     * @param cycle 循环次数，上电检测20组，周期检测5组
     * @param limit 允许错误次数，上电检测2次，周期检测1次
     * @return
     */
    public static boolean randomCheck(int cycle, int limit) {
        Logger.e("randomCheck Start");
        int err = 0;
        for (int i = 0; i < cycle; i++) {
            StringBuilder randomStr = new StringBuilder();
            for (int j = 0; j < 10; j++) {
                randomStr.append(randomNum_get(125));
            }
            Logger.e("randomCheck i:" + i + randomStr);
            if (!PokerTest.randomTest(randomStr.toString(), 2)) {
                Logger.e("pokerTestFail" + i);
                err++;
            } else {
                Logger.e("pokerTestSuccess" + i);
            }
        }
        if (err >= limit) {
            err = 0;
            for (int i = 0; i < cycle; i++) {
                StringBuilder randomStr = new StringBuilder();
                for (int j = 0; j < 10; j++) {
                    randomStr.append(randomNum_get(125));
                }
                Logger.e("randomCheck i:" + i + randomStr);
                if (!PokerTest.randomTest(randomStr.toString(), 2)) {
                    Logger.e("pokerReTestFail" + i);
                    err++;
                } else {
                    Logger.e("pokerReTestSuccess" + i);
                }
            }
            return err < limit;
        } else {
            return true;
        }
    }

    /**
     * 获取保护密钥明文
     */
    public static String getProtectKey() {
        String key = SysConf.getProtectEncryptKey();//获取保护密钥加密密钥
        Logger.i("SMAction getProtectKey key:" + key);
        String data = SysConf.getProtectKey();
        Logger.i("SMAction getProtectKey data:" + data);
        String protectKey = sm4_Decrypt(data, data.length(), key, key.length());
        Logger.i("SMAction getProtectKey protectKey:" + protectKey);
        return protectKey;
    }

    /**
     * 使用保护密钥加密其他密钥
     *
     * @param beEncryptKey
     * @return
     */
    public static String useProtectKeyEncrypt(String beEncryptKey) {
        String key = SysConf.getProtectEncryptKey();//获取保护密钥加密密钥明文
        Logger.i("SMAction getProtectKey key:" + key);
        String protectKeyChip = SysConf.getProtectKey();///获取保护密钥密文
        Logger.i("SMAction getProtectKey data:" + protectKeyChip);
        String protectKey = sm4_Decrypt(protectKeyChip, protectKeyChip.length(), key, key.length());
        Logger.i("SMAction getProtectKey protectKey:" + protectKey);
        String encryptKey = sm4_Encrypt(beEncryptKey, beEncryptKey.length(), protectKey, protectKey.length());//加密其他密钥
        return encryptKey;
    }

    /**
     * 使用保护密钥解密其他密钥
     *
     * @param beDecryptKey
     * @return
     */
    public static String useProtectKeyDecrypt(String beDecryptKey) {
        String key = SysConf.getProtectEncryptKey();//获取保护密钥加密密钥明文
        Logger.i("SMAction getProtectKey key:" + key);
        String protectKeyChip = SysConf.getProtectKey();///获取保护密钥密文
        Logger.i("SMAction getProtectKey data:" + protectKeyChip);
        String protectKey = sm4_Decrypt(protectKeyChip, protectKeyChip.length(), key, key.length()); //对保护密钥使用保护加密密钥进行加密
        Logger.i("SMAction getProtectKey protectKey:" + protectKey);
        String decryptKey = sm4_Decrypt(beDecryptKey, beDecryptKey.length(), protectKey, protectKey.length());//解密密钥
        Logger.i("SMAction decryptKey:" + decryptKey);
        return decryptKey;
    }

    /**
     * SM4  MAC  CBC
     *
     * @param input_data 需要加密的数据
     * @param inputlen   数据长度
     * @param input_key  工作密钥
     * @param keylen     工作密钥长度
     * @return
     */
    public static String sm4_MAC_cbc(String input_data, int inputlen, String input_key, int keylen, String input_iv) {
        try {
            //构造参数
            JSONObject params = new JSONObject();
            params.put("input_data", input_data);
            params.put("intput_data_length", inputlen);
            params.put("input_key", input_key);
            params.put("intput_key_length", keylen);
            params.put("input_IV", input_iv);

            CmdResonse cr = SysAccessor.sendOneCmd("sm4_MAC_cbc", params, DEFULT_TIMEOUT);
            if (cr.getResult()) {
                JSONObject jsdata = cr.getJsdata();
                String data = jsdata.optString("encrypt_data");
                int len = jsdata.optInt("encrypt_len");  //底层加解密数据 实际长度
                return data;
            } else {
                Logger.e(cr.getErr_msgs());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SM4 分组加密，输入数据每16字节加密一次，可加密长数据
     *
     * @param input_data 需要加密的数据
     * @param inputlen   数据长度
     * @param input_key  工作密钥
     * @param keylen     工作密钥长度
     * @return
     */
    public static String sm4_EncrypEx(String input_data, int inputlen, String input_key, int keylen) {
        String data = "";
        try {
            //构造参数
            for (int i = 0; i < inputlen; i += 32) {
                JSONObject params = new JSONObject();

                String input = input_data.substring(i, i + 32);
                // Logger.i("input  :"+ input );
                params.put("input_data", input);
                params.put("intput_data_length", 32);
                //  Logger.i("input_key  :"+ input_key );
                params.put("input_key", input_key);
                params.put("intput_key_length", keylen);

                CmdResonse cr = SysAccessor.sendOneCmd("sm4_encrypt_ebc", params, DEFULT_TIMEOUT);
                if (cr.getResult()) {
                    JSONObject jsdata = cr.getJsdata();
                    data += jsdata.optString("encrypt_data");
                } else {
                    Logger.e(cr.getErr_msgs());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
