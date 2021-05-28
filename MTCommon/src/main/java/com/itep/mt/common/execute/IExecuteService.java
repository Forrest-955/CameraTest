package com.itep.mt.common.execute;

import android.os.RemoteException;
import com.itep.mt.common.ipc.ActionRequest;
import com.itep.mt.common.ipc.IActionResponseListener;

/**
 * 执行服务类接口
 */
public interface IExecuteService {

	/**
	 * 执行
	 * @param req 请求
	 * @param listener  响应监听
	 * @throws RemoteException
     */
	  void execute(ActionRequest req, IActionResponseListener listener) throws RemoteException;

}
