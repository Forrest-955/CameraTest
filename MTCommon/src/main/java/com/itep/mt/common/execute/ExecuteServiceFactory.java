package com.itep.mt.common.execute;

import android.content.Context;
import android.util.Xml;

import com.itep.mt.common.constant.VersionNameConstant;
import com.itep.mt.common.sys.SysConf;
import com.itep.mt.common.util.Logger;
import com.itep.mt.common.util.ReflectionUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行服务工厂类
 *
 */
public class ExecuteServiceFactory {

    private static final String EXECUTE_CONF_PATH="conf/executeService.xml"; //执行配置目录地址

    private static  final String XML_ENCODING="UTF-8"; //XML默认编码

    private static final String DEF_VERSION_NAME= VersionNameConstant.GENERIC; //默认版本信息

	private static IExecuteService executeService; //执行类

    private static Map<String,String> executeMap=new HashMap<String,String>(); //执行容器

    /**
     * 初始化配置资源
     * @param context  上下文
     */
    public static void init(Context context){
        try {
            //从配置文件获取执行相关类信息
            InputStream is = context.getAssets().open(EXECUTE_CONF_PATH);
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(is,XML_ENCODING);
            int event = pullParser.getEventType();
            //填充执行容器
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        executeMap.clear();
                        break;
                    case XmlPullParser.START_TAG:
                        if ("service".equals(pullParser.getName())) {
                            String namespace=pullParser.getNamespace();
                            String name=pullParser.getAttributeValue(namespace,"name");
                            String value=pullParser.getAttributeValue(namespace,"class");
                            //从版本常量类中获取版本信息
                            Field field=ReflectionUtils.findField(VersionNameConstant.class,name.trim());
                            if(field!=null){
                                String versionName= (String) ReflectionUtils.getField(field,String.class);
                                executeMap.put(versionName,value.trim());
                            }
                        }
                        break;
                }
                event = pullParser.next();
            }
        } catch (Exception e) {
            Logger.e("执行管理类初始化失败!",e);
        }
    }

	/**
     * 获取执行类服务
     */
    public static IExecuteService getExecuteService() {
        if(executeService==null){
            String versionName=SysConf.getAppVersionName();
            if(!executeMap.containsKey(versionName)){
                versionName=DEF_VERSION_NAME;
            }
            String className=executeMap.get(versionName);
            try {
                Class<?> cls = Class.forName(className);
                executeService= (IExecuteService) cls.newInstance();
            } catch (Exception e) {
                Logger.e("实例化失败："+className);
            }
        }
        return executeService;
    }
	
}
