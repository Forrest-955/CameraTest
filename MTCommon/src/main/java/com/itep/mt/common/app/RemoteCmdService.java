package com.itep.mt.common.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.itep.mt.common.constant.AppConstant;
import com.itep.mt.common.ipc.ICmdCallback;
import com.itep.mt.common.ipc.IDispatchService;
import com.itep.mt.common.util.ContextUtil;
import com.itep.mt.common.util.Logger;

/**
 * 各个应用程序在启动时都应该调用该类的init函数，它将建立与调度之间的通信连接
 */
public class RemoteCmdService implements ServiceConnection {

    //单一实例
    private static RemoteCmdService _instance;

    private RemoteCmdService() {
    }

    private IDispatchService service;    //调度提供的服务
    private ICmdCallback cbCmd;            //应用程序的命令回调函数

    /**
     * 创建与调度程序的连接
     */
    public static void init(ICmdCallback cb) {
        if (_instance == null) {
            _instance = new RemoteCmdService();
            _instance.cbCmd = cb;

            Intent intent = new Intent(IDispatchService.class.getName());
            intent.setPackage("com.itep.mt.dispatch");
            if (!ContextUtil.getInstance().bindService(intent, _instance, Context.BIND_AUTO_CREATE)) {
                Logger.e("Can't connect to dispatch app");
            }
        }
    }

    /**
     * 断开连接
     */
    public static void uninit() {
        ContextUtil.getInstance().unbindService(_instance);
    }

    /**
     * 通知当前UI界面处于显示状态
     */
    public static void noticeStatusAtRun() {
        noticeStatus(AppConstant.APP_STATUS_RUN_FG);
    }

    /**
     * 通知当前UI界面处于关闭
     */
    public static void noticeStatusAtStop() {
        noticeStatus(AppConstant.APP_STATUS_STOP);
    }

    /**
     * 通知当前处理工作已暂停
     */
    public static void noticeStatusAtPause() {
        noticeStatus(AppConstant.APP_STATUS_PAUSE);
    }

    /**
     * 通知当前状态
     *
     * @param status
     */
    public static void noticeStatus(int status) {
        if (_instance == null) {
            try {
                String apppkg = ContextUtil.getInstance().getPackageName();
                _instance.service.noticeStatus(apppkg, status);
                Logger.i("Notice dispatch: my status is " + status);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.service = IDispatchService.Stub.asInterface(service);

        try {
            //注册
            String apppkg = ContextUtil.getInstance().getPackageName();
            this.service.registerExecutor(apppkg, cbCmd);
            Logger.i("Connect to dispatch successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        //反注册
        try {
            String apppkg = ContextUtil.getInstance().getPackageName();
            this.service.unregisterExecuror(apppkg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.service = null;
        Logger.e("Disconnect from dispatch!");
    }

}
