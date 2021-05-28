package com.itep.mt.common.util;

import android.os.SystemClock;
import android.text.TextUtils;

import com.itep.mt.common.constant.AppConstant;
import com.itep.mt.common.constant.version.GenericConstant;
import com.itep.mt.common.sys.SMAction;
import com.itep.mt.common.sys.SysConf;

import org.json.JSONObject;

public class GMUtils {

    /**
     * 获取公私钥对
     * @return 成功：json：result，失败：错误码
     */
    public static JSONObject getSM2keyPair() {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);

            if (SMAction.sm2_CreateKeys()) {//创建
                JSONObject jsdata = SMAction.sm2_GetKeyPair();
                Logger.i("jsData:" + jsdata);
                String publickey = jsdata.optString("publickey");
                String privatekey = jsdata.optString("privatekey");
                ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PUBLICKKEY, publickey);
                ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PRIVATEKEY, privatekey);
                Logger.i("publickeys:" + publickey + "||||privatekey:" + privatekey);
                paramsObj.put("result", true);
                paramsObj.put("data", publickey + privatekey);//返回公私
                return paramsObj;
            } else {
                paramsObj.put("errcode", 1);//没有安全芯片
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return paramsObj;
    }

    /**
     * 导入公私钥对
     * @param pulicKey
     * @param privateKey
     * @return 成功：json:result, 失败：错误码
     */
    public static JSONObject importSM2keyPair(String pulicKey, String privateKey) {

        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            Logger.i("公钥:" + pulicKey);
            Logger.i("私钥:" + privateKey);
            if (SMAction.sm2_InputKeys(pulicKey, privateKey)) {
                paramsObj.put("result", true);
                paramsObj.put("data", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paramsObj;
    }

    /**
     * SM2加密
     * @param input
     * @param inputLen
     * @return 成功：json：result, 失败：错误码
     */
    public static JSONObject SM2encode(String input, int inputLen) {

        JSONObject paramsObj = new JSONObject();
        try {
            String publickey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PUBLICKKEY, "");
            String privatekey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PRIVATEKEY, null);
            importSM2keyPair(publickey, privatekey);
            paramsObj.put("result", false);
            String output = SMAction.sm2_Encrypt(input, inputLen);
            if (output != null) {
                paramsObj.put("result", true);
                Logger.i("data::" + output);
                paramsObj.put("data", output);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramsObj;
    }

    /**
     * SM2解密
     * @param input
     * @param inputLen
     * @return Json：result
     */
    private JSONObject SM2decode(String input, int inputLen){
        JSONObject paramsObj = new JSONObject();
        try {
            String publicKey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PUBLICKKEY, "");
            String privateKey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PRIVATEKEY, null);
            importSM2keyPair(publicKey, privateKey);
            paramsObj.put("result", false);
            String output = SMAction.sm2_Decrypt(input, inputLen);
            if (output != null) {
                paramsObj.put("result", true);
                Logger.i("data:" + output);
                paramsObj.put("data", output);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return paramsObj;
    }

    /**
     * SM2签名
     * @param content
     * @return Json:result
     */
    public static JSONObject SM2createSign(String content) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);

            String publickey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PUBLICKKEY, "");
            String privatekey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PRIVATEKEY, null);
            importSM2keyPair(publickey, privatekey);
            JSONObject jsdata = SMAction.sm2_sign(content);
            if (jsdata != null) {
                String signatrue_data = jsdata.optString("signatrue_data");
                String hash_data = jsdata.optString("hash_data");
                Logger.i("signatrue_data:" + signatrue_data + "||||hash_data:" + hash_data);
                paramsObj.put("result", true);
                paramsObj.put("data", signatrue_data + hash_data);//签名数据+hash值
                return paramsObj;
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramsObj;
    }

    /**
     * SM2验证签名
     * @param input
     * @param signatrue_data
     * @return
     */
    public static JSONObject sm2_verity_sign(String input, String signatrue_data) {
        JSONObject paramsObj = new JSONObject();
        try {
            String publickey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PUBLICKKEY, "");
            String privatekey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PRIVATEKEY, null);
            importSM2keyPair(publickey, privatekey);
            paramsObj.put("result", false);
            if (SMAction.sm2_verity_sign(input, signatrue_data)) {
                paramsObj.put("result", true);
                paramsObj.put("data", "");//签名数据+hash值
                return paramsObj;
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramsObj;
    }

    /**
     * SM3杂凑值
     * @param input
     * @param inputLen
     * @return JSON:result
     */
    public static JSONObject SM3Hash(String input, int inputLen) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            String output = SMAction.sm3_Hash(input, inputLen);
            if (output != null) {
                paramsObj.put("result", true);
                Logger.i("data::" + output);
                paramsObj.put("data", output);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramsObj;
    }

    /**
     * 获取指定长度的随机数
     * @param ramdomLen
     * @return JSON:result
     */
    public static JSONObject getRandomNum(int ramdomLen) {
        JSONObject paramsObj = new JSONObject();
        //周期性检测
        long lastCheckTime= SystemClock.elapsedRealtime();
        if (SystemClock.elapsedRealtime()-lastCheckTime>24*60*60*1000){
            boolean result= SMAction.randomCheck(5,1);
            if (!result) {
                return paramsObj;
            }
        }
        try {
            paramsObj.put("result", false);
            String output = SMAction.randomNum_getChecked(ramdomLen);
            if (output != null&&!TextUtils.isEmpty(output)) {
                paramsObj.put("result", true);
                Logger.i("data::" + output);
                paramsObj.put("data", output);
            } else {
                Logger.i("data::" + "");

            }
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
        return paramsObj;
    }

    /**
     * SM4加密ecb
     * @param input
     * @param inputLen
     * @param key
     * @param keyLen
     * @return
     */
    public static JSONObject SM4encode_ecb(String input, int inputLen, String key, int keyLen) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            String output = SMAction.sm4_Encrypt(input, inputLen, key, keyLen);
            if (output != null) {
                paramsObj.put("result", true);
                Logger.i("data::" + output);
                paramsObj.put("data", output);
            } else {

            }
        } catch (Exception ex) {

        }
        return paramsObj;
    }

    /**
     * SM4解密ecb
     * @param input
     * @param inputLen
     * @param key
     * @param keyLen
     * @return
     */
    public static JSONObject SM4decode_ecb(String input, int inputLen, String key, int keyLen) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            String output = SMAction.sm4_Decrypt(input, inputLen, key, keyLen);
            if (output != null) {
                paramsObj.put("result", true);
                Logger.i("data::" + output);
                paramsObj.put("data", output);
            } else {

            }
        } catch (Exception ex) {

        }
        return paramsObj;
    }

    /**
     * SM4mac运算
     * @param input
     * @param inputLen
     * @param key
     * @param keyLen
     * @param inpu_iv
     * @return
     */
    public static JSONObject SM4MAC_cbc(String input, int inputLen, String key, int keyLen, String inpu_iv) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            String output = SMAction.sm4_MAC_cbc(input, inputLen, key, keyLen, inpu_iv);
            if (output != null) {
                paramsObj.put("result", true);
                Logger.i("data::" + output);
                paramsObj.put("data", output);
            } else {

            }
        } catch (Exception ex) {

        }
        return paramsObj;
    }

    /**
     * SM4加密cbc
     * @param input
     * @param inputLen
     * @param key
     * @param keyLen
     * @param inpu_iv
     * @return
     */
    public static JSONObject SM4encode_cbc(String input, int inputLen, String key, int keyLen, String inpu_iv) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            String output = SMAction.sm4_Encryp_cbc(input, inputLen, key, keyLen, inpu_iv);
            if (output != null) {
                paramsObj.put("result", true);
                Logger.i("data::" + output);
                paramsObj.put("data", output);
            } else {

            }
        } catch (Exception ex) {

        }
        return paramsObj;
    }

    /**
     * SM4解密cbc
     * @param input
     * @param inputLen
     * @param key
     * @param keyLen
     * @param input_iv
     * @return
     */
    public static JSONObject SM4decode_cbc(String input, int inputLen, String key, int keyLen, String input_iv) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            String output = SMAction.sm4_Decrypt_cbc(input, inputLen, key, keyLen, input_iv);
            if (output != null) {
                paramsObj.put("result", true);
                Logger.i("data::" + output);
                paramsObj.put("data", output);
            } else {

            }
        } catch (Exception ex) {

        }
        return paramsObj;
    }

    /**
     * 下载随机数
     * @param randomNum
     * @return JSON:result
     */
    public static JSONObject SetRandomNum(String randomNum) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            //读取KEY
            String interKey = SysConf.getGMInternalKey();
            Logger.i("randomNum " + randomNum + "     key:" + interKey);
            String output = SMAction.HMACSM3(interKey, randomNum);
            Logger.i("output " + output);
            paramsObj.put("result", true);
            paramsObj.put("data", output);
        } catch (Exception ex) {

        }
        return paramsObj;
    }

    /**
     * 内部认证结果
     * @param result
     * @return
     */
    public static JSONObject SetResult(int result) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            //保存结果
            ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_INTERNAL_AUTH, result);
            paramsObj.put("result", true);
            paramsObj.put("data", "");
        } catch (Exception ex) {

        }
        return paramsObj;
    }

    /**
     * 获取无指定长度随机数
     * @return
     */
    public static JSONObject getRandomNum() {

        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            paramsObj.put("data", 1);
            int result = ConfFile.getConfInteger(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_INTERNAL_AUTH, 0);//获取内部认证结果
            int count = ConfFile.getConfInteger(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_COUNT, GenericConstant.GM_EXTERNAL_DEFAULT_COUNT);//获取外部错误计数
            if (result == 1 && count != 0) {
                String output = SMAction.randomNum_getChecked(16);
                if (output != null&&!TextUtils.isEmpty(output)) {
                    ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_RANDOMNUM, output);//保存随机数,下次校验值的时候进行判断是否为空
                    paramsObj.put("result", true);
                    Logger.i("data::" + output);
                    paramsObj.put("data", output);
                }
            }
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
        return paramsObj;

    }

    /**
     * 校验随机数HASH值
     * @param input_hash
     * @return
     */
    public static JSONObject checkRandomNumHash(String input_hash) {
        JSONObject paramsObj = new JSONObject();
        try {
            Logger.i("input_hash::" + input_hash);
            paramsObj.put("result", false);
            String randomNum = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_RANDOMNUM, null);//获取内部认证结果
            if (randomNum != null) {
                String interKey = SysConf.getGMExternalKey();
                Logger.i("randomNum " + randomNum + "     key:" + interKey);
                String output = SMAction.HMACSM3(interKey,randomNum);
                Logger.i("output " + output);
                Logger.i("data::" + output);
                if (output != null) {
                    if (output.equals(input_hash)) {
                        ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_AUTH, 1);//保存外部认证结果
                        ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_COUNT, GenericConstant.GM_EXTERNAL_DEFAULT_COUNT);//保存外部认证结果
                        paramsObj.put("result", true);

                        paramsObj.put("data", "");
                    } else {
                        ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_AUTH, 0);
                        int count = ConfFile.getConfInteger(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_COUNT, GenericConstant.GM_EXTERNAL_DEFAULT_COUNT);
                        ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_COUNT, --count);//保存外部认证结果
                        paramsObj.put("data", 2);//校验不通过
                    }
                } else {
                    paramsObj.put("data", 1);//没有加密芯片

                }
            }
        } catch (Exception ex) {

        }
        return paramsObj;
    }

    /**
     * 初始化
     * @return
     */
    public static JSONObject GMinit() {

        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            //            int result = ConfFile.getConfInteger(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_INTERNAL_AUTH, 0);//获取内部认证结果
            //            if (result == 1) {
            //  Logger.i("认证通过，可以进行初始化");
            Logger.i("MACKEY：" + GenericConstant.GM_MACKEYDEFAULT);
            Logger.i("mianKey：" + GenericConstant.GM_MAINKEYDEFAULT);
            Logger.i("内部认证密钥：" + GenericConstant.GM_INTERNAL_AUTH_KEYDEFAULT);
            Logger.i("外部认证密钥：" + GenericConstant.GM_EXTERNAL_AUTH_KEYDEFAULT);

            boolean exte = ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_COUNT, GenericConstant.GM_EXTERNAL_DEFAULT_COUNT);//外部认证错误计初始化

            //开始初始化各种参数
            paramsObj.put("result", true);
            SysConf.setGMMacKey(GenericConstant.GM_MACKEYDEFAULT);
            SysConf.setGMTramsKey(GenericConstant.GM_TRAMSKEYDEFAULT);
            SysConf.setGMMainKey(GenericConstant.GM_MAINKEYDEFAULT);
            SysConf.setGMInternalKey(GenericConstant.GM_INTERNAL_AUTH_KEYDEFAULT);
            SysConf.setGMExternalKey(GenericConstant.GM_EXTERNAL_AUTH_KEYDEFAULT);
            //            ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_AUTH_KEY, GenericConstant.GM_EXTERNAL_AUTH_KEYDEFAULT);
            //ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF,GenericConstant.GM_MACKEY,"");

            paramsObj.put("data", "");//返回空
            //            } else {
            //                params.put("data", 1);//没有加密芯片
            //
            //            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return paramsObj;

    }

    /**
     * 获取主密钥
     * @return
     */
    public static JSONObject getMainKey() {

        JSONObject paramsObj = new JSONObject();
        try {
            getSM2keyPair();
            paramsObj.put("result", false);
            String mainkey = SysConf.getGMMainKey();//获取mainkey
            if (mainkey != null) {
                String output = "";
                String mainkeyhash = SMAction.sm3_Hash(mainkey, 16);
                Logger.i("mainkeyhash:" + mainkeyhash);
                output += mainkeyhash;
                String publickKey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PUBLICKKEY, null);//获取mainkey
                //SMAction.sm2_GetKeys();
                //  Logger.i("publickKey:"+publickKey);
                String publickKeyEN = SMAction.sm4_EncrypEx(publickKey, 128, mainkey, 16);
                output += publickKeyEN;
                Logger.i("publickKeyEN:" + publickKeyEN);
                String publickKeyHash = SMAction.sm3_Hash(publickKey, 64);
                Logger.i("publickKeyHash:" + publickKeyHash);
                output += publickKeyHash;
                if (mainkeyhash != null) {
                    paramsObj.put("result", true);
                    paramsObj.put("data", output);
                } else {
                    paramsObj.put("data", 1);//没有加密芯片

                }
            }
        } catch (Exception ex) {

        }
        return paramsObj;

    }

    /**
     * 下载主密钥
     * @param mainKeyEn
     * @param mainKeyHash
     * @return
     */
    public static JSONObject setMainKey(String mainKeyEn, String mainKeyHash) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            paramsObj.put("data", 2);//校验错误
            String publickey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PUBLICKKEY, "");
            String privatekey = ConfFile.getConfString(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_SM2PRIVATEKEY, null);
            importSM2keyPair(publickey, privatekey);//导入主密钥，因为在自检的时候会把公私用替换
            String mainkey = SysConf.getGMMainKey();//获取mainkey
            if (mainkey != null) {
                String mainKeyNew = SMAction.sm2_Decrypt(mainKeyEn, 224);
                if (mainKeyNew != null) {
                    Logger.i("mainKeyNew:" + mainKeyNew);
                    String output = SMAction.sm3_Hash(mainKeyNew, 16);
                    Logger.i("SM3output:" + output);
                    if (output.equals(mainKeyHash)) {
                        paramsObj.put("result", true);
                        paramsObj.put("data", output);
                        SysConf.setGMMainKey(mainKeyNew);
                    } else {
                        paramsObj.put("errcode", 3);//校验错误
                    }
                } else {
                    paramsObj.put("errcode", 3);//校验错误

                }

            }
        } catch (Exception ex) {

        }
        return paramsObj;
    }

    /**
     * 下载工作密钥
     * @param type
     * @param mainKeyHash
     * @param workKeyHash
     * @param workKeyEn
     * @return
     */
    public static JSONObject setWorkKey(int type, String mainKeyHash, String workKeyHash, String workKeyEn) {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", false);
            String mainkey = SysConf.getGMMainKey();//获取mainkey
            Logger.i("mainKey" + mainkey);
            if (mainkey != null) {
                String output = SMAction.sm3_Hash(mainkey, 16);
                Logger.i("mainKeyhash" + output);
                if (output.equals(mainKeyHash)) {
                    Logger.i("----------------");
                    String WorkKey = SMAction.sm4_Decrypt(workKeyEn, 16, mainkey, 16);
                    Logger.i("WORKKEY" + WorkKey);
                    String outputHASH = SMAction.sm3_Hash(WorkKey, 16);
                    Logger.i("outputHASH" + outputHASH);
                    if (outputHASH.equals(workKeyHash)) {
                        Logger.i("----------------");
                        paramsObj.put("result", true);
                        paramsObj.put("data", "00");
                        if (type==8){//下载传输密钥，特殊处理：返回时使用旧传输密钥，否则验证不通过
                            paramsObj.put("type",8);
                        }
                        saveWorkKey(type, WorkKey);
                    } else {
                        paramsObj.put("errcode", 3);//
                    }
                } else {
                    paramsObj.put("errcode", 2);//校验错误
                }

            }
        } catch (Exception ex) {

        }
        return paramsObj;
    }

    private static void saveWorkKey(int type, String workKey) {
        //        String wokeKeyTypeStr = GenericConstant.GM_WORKKEY;//默认是密码键盘的工作密钥
        switch (type) {
            case 2:
                //                wokeKeyTypeStr = GenericConstant.GM_WORKKEY;
                ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_WORKKEY, workKey);
                Logger.i("GMExecute wokekeyTypeStr: GenericConstant.GM_WORKKEY");
                break;
            case 3:
                //                wokeKeyTypeStr = GenericConstant.GM_MACKEY;
                SysConf.setGMMacKey(workKey);
                Logger.i("GMExecute wokekeyTypeStr: GenericConstant.GM_MACKEY");
                break;
            //            case 4:
            //                wokeKeyTypeStr = GenericConstant.GM_TPK;
            //                Logger.i("GMExecute wokekeyTypeStr: GenericConstant.GM_TPK");
            //                break;
            //            case 5:
            //                wokeKeyTypeStr = GenericConstant.GM_AIK;
            //                Logger.i("GMExecute wokekeyTypeStr: GenericConstant.GM_AIK");
            //                break;
            case 6:
                Logger.i("GMExecute wokekeyTypeStr: GenericConstant.GM_EXTERNAL_AUTH_KEY");
                //                wokeKeyTypeStr = GenericConstant.GM_EXTERNAL_AUTH_KEY;
                SysConf.setGMExternalKey(workKey);
                break;
            case 7:
                //                wokeKeyTypeStr = GenericConstant.GM_INTERNAL_AUTH_KEY;
                SysConf.setGMInternalKey(workKey);
                Logger.i("GMExecute wokekeyTypeStr: GenericConstant.GM_INTERNAL_AUTH_KEY");
                break;
            case 8:
                //                wokeKeyTypeStr = GenericConstant.GM_TRAMSKEY;
                SysConf.setGMTramsKey(workKey);
                Logger.i("GMExecute wokekeyTypeStr: GenericConstant.GM_TRAMSKEY");
                break;
        }
        //        return wokeKeyTypeStr;
    }


    /***
     * sm2算法自检
     * @return
     */

    public static JSONObject SM2Check() {
        JSONObject paramsObj = new JSONObject();
        try {
            if (SMAction.sm2Check()) {
                paramsObj.put("result", true);
                paramsObj.put("data", 1);
                Logger.i("GMExecute SM2Check:sm2自检成功");
            } else {
                Logger.i("GMExecute SM2Check:sm2自检失败");
            }
        } catch (Exception ex) {
            Logger.i("GMExecute SM2Check ex：" + ex.getMessage());
        }
        return paramsObj;
    }


    /***
     * sm2数字算法自检
     * @return
     */

    public static JSONObject SM2SignCheck() {
        JSONObject paramsObj = new JSONObject();
        try {
            if (SMAction.sm2SignCheck()) {
                paramsObj.put("result", true);
                paramsObj.put("data", 1);
                Logger.i("GMExecute SM2SignCheck:数字签名自检成功");
            } else {
                Logger.i("GMExecute SM2SignCheck:数字签名自检失败");
            }
        } catch (Exception ex) {
            Logger.i("GMExecute SM2SignCheck ex：" + ex.getMessage());
        }
        return paramsObj;
    }

    /***
     * sm3数字算法自检
     * @return
     */

    public static JSONObject SM3Check() {
        JSONObject paramsObj = new JSONObject();
        try {
            if (SMAction.sm3Check()) {
                paramsObj.put("result", true);
                paramsObj.put("data", 1);
                Logger.i("GMExecute SM3Check:自检成功");
            } else {
                Logger.i("GMExecute SM3Check:自检失败");
            }
        } catch (Exception ex) {
            Logger.i("GMExecute SM3Check ex：" + ex.getMessage());
        }
        return paramsObj;
    }

    /***
     * SM4CBC算法自检
     * @return
     */

    public static JSONObject SM4CBCCheck() {
        JSONObject paramsObj = new JSONObject();
        try {
            if (SMAction.sm4CBCCheck()) {
                paramsObj.put("result", true);
                paramsObj.put("data", 1);
                Logger.i("GMExecute SM4CBCCheck:自检成功");
            } else {
                Logger.i("GMExecute SM4CBCCheck:自检失败");
            }
        } catch (Exception ex) {
            Logger.i("GMExecute SM4CBCCheck ex：" + ex.getMessage());
        }
        return paramsObj;
    }

    /***
     * SM4CBC算法自检
     * @return
     */

    public static JSONObject SM4ECBCheck() {
        JSONObject paramsObj = new JSONObject();
        try {
            if (SMAction.sm4ECBCheck()) {
                paramsObj.put("result", true);
                paramsObj.put("data", 1);
                Logger.i("GMExecute SM4ECBCheck:自检成功");
            } else {
                Logger.i("GMExecute SM4ECBCheck:自检失败");
            }
        } catch (Exception ex) {
            Logger.i("GMExecute SM4ECBCheck ex：" + ex.getMessage());
        }
        return paramsObj;
    }


    public static JSONObject claerZero() {
        JSONObject paramsObj = new JSONObject();
        try {
            paramsObj.put("result", true);
            paramsObj.put("data", 1);
            ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_MACKEY, GenericConstant.GM_CLEA_ZERO);
            ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_TRAMSKEY, GenericConstant.GM_CLEA_ZERO);
            ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_MAINKEY, GenericConstant.GM_CLEA_ZERO);
            ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_INTERNAL_AUTH_KEY, GenericConstant.GM_CLEA_ZERO);
            ConfFile.putConf(AppConstant.PATH_GMPINPAD_CONF, GenericConstant.GM_EXTERNAL_AUTH_KEY, GenericConstant.GM_CLEA_ZERO);
        } catch (Exception ex) {
            Logger.i("GMExecute SM4PinBlockChekck ex：" + ex.getMessage());
        }
        return paramsObj;
    }
}
