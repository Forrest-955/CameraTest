package com.itep.mt.common.app;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 实现对各业务应用界面activity的控制，如立即关闭、更新显示元素或其他功能
 * 原理:
 * （1）每个业务应用Activity在启动时，向AppController申请注册，其这只是记录；在退出时，进行反注册
 * （2）当Binder线程收到消息后，调用closeActivity或postExtraData，来传递要求
 */
public class AppController {
    private static ArrayList<BaseActivity> activites = new ArrayList<BaseActivity>();        //activity列表

    /**
     * 注册Activity对象，当Activity创建时执行。一般在UI主线程执行，但需要与binder线程同步
     *
     * @param activity 界面对象
     */
    public static void registerActivity(BaseActivity activity) {
        synchronized (activites) {
            activites.add(activity);
        }
    }

    /**
     * 反注册Acctivity对象，当activity退出时执行。一般在UI主线程执行，但需要与binder线程同步
     *
     * @param activity
     */
    public static void unregisterActivity(BaseActivity activity) {
        synchronized (activites) {
            activites.remove(activity);
        }
    }

    /**
     * 通知所有activity关闭，一般在binder线程中执行，但需要与UI主线程同步
     */
    public static void closeActivity() {
        synchronized (activites) {
            Iterator<BaseActivity> it = activites.iterator();
            while (it.hasNext()) {
                BaseActivity act = it.next();
                act.postNoticeMessage(BaseActivity.MSG_CLOSE);
            }
        }
    }

    /**
     * 判断是否有activity在启动
     */
    public static boolean getActivity() {
        synchronized (activites) {
            Iterator<BaseActivity> it = activites.iterator();
            while (it.hasNext()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 向所有activity（一般只会有一个）传递数据，，一般在binder线程中执行， 但需要与UI主线程同步
     *
     * @param data_type 标识data的类型
     * @param data      实际的数据
     */
    public static void postExtraData(int data_type, Object data) {
        synchronized (activites) {
            Iterator<BaseActivity> it = activites.iterator();
            while (it.hasNext()) {
                BaseActivity act = it.next();
                act.postNoticeMessage(BaseActivity.MSG_DATA, data_type, data);
            }
        }
    }
}
