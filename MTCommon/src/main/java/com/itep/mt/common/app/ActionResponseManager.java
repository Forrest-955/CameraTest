package com.itep.mt.common.app;

import java.util.ArrayList;

import com.itep.mt.common.ipc.ActionResponse;
import com.itep.mt.common.ipc.IActionResponseListener;
import com.itep.mt.common.util.Logger;

/**
 * 记录应用得到的IActionResponseListener对象，并提供接口供应用调用。
 */
public class ActionResponseManager {
    private ArrayList<RemoteListener> listeners;

    static class RemoteListener {
        private String msg;                    //消息类型
        private IActionResponseListener ls;    //监听器
        private long timeCreated;            //创建时间

        public RemoteListener(String msg, IActionResponseListener ls) {
            super();
            this.msg = msg;
            this.ls = ls;
            this.timeCreated = System.currentTimeMillis();
        }

        public String getMsg() {
            return msg;
        }

        public IActionResponseListener getLs() {
            return ls;
        }

        public long getTimeCreated() {
            return timeCreated;
        }
    }

    //单例模式
    private static ActionResponseManager _instance;

    private ActionResponseManager() {
        listeners = new ArrayList<RemoteListener>();
    }

    /**
     * 加入一个监听器
     *
     * @param msg      消息类型
     * @param listener 监听器
     */
    public static void putListener(String msg, IActionResponseListener listener) {
        if (_instance == null) {
            _instance = new ActionResponseManager();
        }
        _instance.listeners.add(new RemoteListener(msg, listener));
    }

    /**
     * 执行一个响应
     *
     * @param msg 消息类型
     * @param res 返回的消息
     */
    public static void response(String msg, ActionResponse res) {
        ArrayList<RemoteListener> listeners = _instance.listeners;

        //遍历 ，执行对应的响应
        for (int i = 0; i < listeners.size(); i++) {
            RemoteListener rl = listeners.get(i);
            if (rl.getMsg().compareTo(msg) == 0) {
                try {
                    if (res == null) {
                        res = new ActionResponse(msg);
                    }
                    rl.getLs().onResponse(res);
                    Logger.i("Response " + res.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listeners.remove(i);
                break;
            }
        }

        //遍历，去除所有过期的响应
        for (int i = 0; i < listeners.size(); i++) {
            RemoteListener rl = listeners.get(i);
            if (System.currentTimeMillis() - rl.getTimeCreated() > 600000) {//大于十分钟，表示过期
                try {
                    rl.getLs().onResponse(new ActionResponse(rl.getMsg()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listeners.remove(i);
                i--;
            }
        }
    }

    /**
     * 执行一个响应（无需删除监听，则用三参数方法）
     *
     * @param msg      消息类型
     * @param res      返回的消息
     * @param isDelete 是否需要删除该监听
     */
    public static void response(String msg, ActionResponse res, boolean isDelete) {
        ArrayList<RemoteListener> listeners = _instance.listeners;

        //遍历 ，执行对应的响应
        for (int i = 0; i < listeners.size(); i++) {
            RemoteListener rl = listeners.get(i);
            if (rl.getMsg().compareTo(msg) == 0) {
                try {
                    if (res == null) {
                        res = new ActionResponse(msg);
                    }
                    rl.getLs().onResponse(res);
                    Logger.i("Response " + res.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isDelete) {
                    listeners.remove(i);
                }
                break;
            }
        }

        //遍历，去除所有过期的响应
        for (int i = 0; i < listeners.size(); i++) {
            RemoteListener rl = listeners.get(i);
            if (System.currentTimeMillis() - rl.getTimeCreated() > 600000) {//大于十分钟，表示过期
                try {
                    rl.getLs().onResponse(new ActionResponse(rl.getMsg()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listeners.remove(i);
                i--;
            }
        }
    }
}
