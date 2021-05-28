// ICmdService.aidl
package com.itep.mt.common.ipc;

import com.itep.mt.common.ipc.ICmdCallback;

interface IDispatchService {
    /**
     *  注册动作执行成员
     */
    void registerExecutor( in String name, in ICmdCallback cb );
    
    /**
     * 反注册动作执行成员应用
     */
    void unregisterExecuror( in String name );
    
    /**
     * 通知状态发生变更
     */
    void noticeStatus( in String name, in int status );
}
