package com.itep.mt.common.execute;

import android.os.RemoteException;

import com.itep.mt.common.app.ActionResponseManager;
import com.itep.mt.common.constant.CmdConstant;
import com.itep.mt.common.constant.version.GenericConstant;
import com.itep.mt.common.dto.BaseDTO;
import com.itep.mt.common.ipc.ActionRequest;
import com.itep.mt.common.ipc.ActionResponse;
import com.itep.mt.common.ipc.IActionResponseListener;
import com.itep.mt.common.util.DataStore;
import com.itep.mt.common.util.Logger;
import com.itep.mt.common.util.ParamUtils;
import com.itep.mt.common.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 执行服务基类
 * Created by hx on 2019/7/12.
 */
public abstract class BaseExecuteService implements  IExecuteService{

    protected DataStore dataStore = new DataStore();     //创建实例化存储对象

    /**
     * 请求
     */
    protected  ActionRequest request;

    /**
     * 响应监听
     */
    protected  IActionResponseListener  resListener;

    /**
     * 消息/方法
     */
    protected  String msg;

    /**
     * 参数
     */
    protected  byte[]  params;

    /**
     * 初始化
     * @param req 请求
     * @param listener 响应监听
     */
    private void init(ActionRequest req, IActionResponseListener listener){
        this.request=req;
        this.resListener=listener;
        this.msg = req.getType();
        this.params= req.getParamByteArray();
        ActionResponseManager.putListener(msg, resListener);//保存响应
    }

    /**
     *
     * @param req 请求
     * @param listener  响应监听
     * @throws RemoteException
     */
    @Override
    public void execute(ActionRequest req, IActionResponseListener listener) throws RemoteException {
        try {
            init(req,listener);
            onMethod();
            //获取所有方法
            Method  invokeMethod=ReflectionUtils.findMethod(this.getClass(),msg,null);
            if(invokeMethod!=null){
                //判断是否有参数，如果有参数则映射对象赋值给入到方法中
                Class[] paramClass=invokeMethod.getParameterTypes();
                Object[] paramObjs=new Object[0];
                if(paramClass!=null&&paramClass.length==1){
                    Object param= ParamUtils.parse(params,paramClass[0]);
                    if(param instanceof BaseDTO){
                        ((BaseDTO) param).setMsgType(msg);
                    }
                    paramObjs=new Object[]{param};
                }
                ReflectionUtils.makeAccessible(invokeMethod);
                ReflectionUtils.invokeMethod(invokeMethod,this,paramObjs);
            }else{
                throw new NoSuchMethodException("No Such Method");
            }
        } catch (Exception e){
            Logger.e("execute "+msg+" method error",e);
            throw new RemoteException(e.getMessage());
        }
    }

    /**
     * 执行方法之前
     */
    protected void onMethod(){

    }


    /**
     * 应答
     * @param id 消息ID
     * @param ret 内容
     * @throws RemoteException
     */
    protected void reply(String id,byte[] ret) {
        if(resListener==null)
            return;
        ActionResponseManager.response(msg,new ActionResponse(id, ret));
        //resListener.onResponse(new ActionResponse(id, ret));
    }

    /**
     * 有参数返回
     * @param ret 内容
     * @throws RemoteException
     */
    protected void replyByParam(byte[] ret){
        reply(GenericConstant.GENERIC_REPLY_PARAM, ret);
    }


    /**
     * 有参数返回
     * @param cmd 指令
     * @param params 参数
     * @throws RemoteException
     */
    protected void replyByParam(byte[] cmd,byte[] params)  {
        byte[] ret=new byte[cmd.length];
        if(params!=null){
            ret = new byte[cmd.length + params.length];
            System.arraycopy(cmd, 0, ret, 0, cmd.length);
            System.arraycopy(params, 0, ret,cmd.length, params.length);
        }
        replyByParam(ret);
    }

    /**
     * 无参返回
     * @param ret  内容
     * @throws RemoteException
     */
    protected void replyByNoParam(byte[] ret)  {
        reply(GenericConstant.GENERIC_REPLY, ret);
    }

    /**
     * 返回成功
     * @throws RemoteException
     */
    protected  void replyOk(){
        replyByNoParam(CmdConstant.CMD_OK);
    }

    /**
     * 返回成功有参数
     * @param params
     * @throws RemoteException
     */
    protected  void replyOk(byte[] params) {
        replyByParam(CmdConstant.CMD_OK,params);
    }


    /**
     * 异常应答
     * @throws RemoteException
     */
    protected void replyError() {
        //TODO 待后期实现
    }


}
