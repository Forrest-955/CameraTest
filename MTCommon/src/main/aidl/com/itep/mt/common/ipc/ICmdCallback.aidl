// ICmdService.aidl
package com.itep.mt.common.ipc;

import com.itep.mt.common.ipc.ActionRequest;
import com.itep.mt.common.ipc.IActionResponseListener;

interface ICmdCallback {
    /**
     *  执行请求
     */
    oneway void execute( in ActionRequest request, in IActionResponseListener listener );
}
