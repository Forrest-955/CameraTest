package com.itep.mt.common.util;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.itep.mt.common.crash.CrashHandler;
import com.itep.mt.common.dto.BaseDTO;
import com.itep.mt.common.sys.SysCommand;

import java.util.List;


public class ContextUtil extends Application {

    private static Application context;//应用程序的上下文


    /**
     * 获取应用上下文实例
     *
     * @return 上下文实例
     */
    public static Application getInstance() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     *
     */
    @Override
    public void onCreate() {
        context = this;
        super.onCreate();

        String pkg = this.getPackageName();
        startService(pkg, pkg + ".MainService");
        //初始化全局异常管理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        initPermission();
    }

    private void initPermission() {
        String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
        String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (checkSelfPermission(readExternalStorage) != PackageManager.PERMISSION_GRANTED){
            //todo 需要root权限
            SysCommand.runCmd("pm grant " + getPackageName() +" "+ readExternalStorage);
        }
        if (checkSelfPermission(writeExternalStorage) != PackageManager.PERMISSION_GRANTED){
            SysCommand.runCmd("pm grant " + getPackageName() +" "+ writeExternalStorage);

        }
    }

    /**
     * 启动一个新的应用activity，使用new task方式创建
     *
     * @param pkg 包名
     * @param cls activity的类全限定名
     */
    public static void startApp(String pkg, String cls) {
        try {
            ComponentName comp = new ComponentName(pkg, cls);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(comp);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(pkg);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动一个本地的activity
     * @param activity 类别
     * @param bundle   bundle
     */
    public static void startLocalActivity(Class<?> activity,Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        startLocalActivity(intent);
    }

    public static void startLocalActivity(Class<?> activity, BaseDTO baseDTO) {
        Bundle bundle=new Bundle();
        bundle.putSerializable(baseDTO.getClass().getSimpleName(),baseDTO);
        startLocalActivity(activity,bundle);
    }


    /**
     * 启动一个本地的activity
     *
     * @param activity 类别
     */
    public static void startLocalActivity(Class<?> activity) {
         startLocalActivity(activity, (Bundle) null);
    }


    /**
     * 启动一个本地的activity
     */
    public static void startLocalActivity(Intent intent) {
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 启动本地服务
     *
     * @param pkg     包名
     * @param clsName 本地服务的类名
     */
    public static void startService(String pkg, String clsName) {
        try {
            Intent intent = new Intent(clsName);
            intent.setPackage(pkg);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            }
            else {
                context.startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭本地服务
     *
     * @param name 服务路径名
     */
    public static void stopServices(String pkg, String name) {
        try {
            //判断服务进程是否存在如果存在将其关闭
            if (isServiceRunning(name)) {
                Logger.i(" dispatch error stop service ");
                Logger.i("Stop app:" + pkg);
                String cmd = "am force-stop " + pkg + " \n";
                SysCommand.runCmd(cmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isServiceRunning(String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


}