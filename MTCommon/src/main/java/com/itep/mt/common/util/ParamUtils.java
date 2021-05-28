package com.itep.mt.common.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.itep.mt.common.annotation.ParamField;
import com.itep.mt.common.annotation.ParamField.*;
import com.itep.mt.common.constant.AppConstant;

/**
 * 参数解析
 * @author hx
 * @date 2019-06-21
 */
public class ParamUtils {

	/**
	 * 获取设置注解报文字段并排序返回
	 * @param obj 对象
	 * @return
	 */
	private static List<Field> getSortMsgFields(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();// 获得所有字段
		List<Field> msgFields = new ArrayList<Field>();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			// 判断是否使用MarshalAs注解
			if (field.isAnnotationPresent(ParamField.class)) {
				msgFields.add(field);
			}
		}
		// 按定义次序排序
		Collections.sort(msgFields, new Comparator<Object>() {
			 @Override
				public int compare(Object arg0, Object arg1) {
			        Field fieldOne = (Field)arg0;
			        Field fieldTwo = (Field)arg1;
				 	ParamField annoOne = fieldOne.getAnnotation(ParamField.class);
				 	ParamField annoTwo = fieldTwo.getAnnotation(ParamField.class);
			        return annoOne.sort()-annoTwo.sort();
			    }
		});
		return msgFields;
	}

	/**
	 *
	 * @param param 参数
	 * @param cls  DTO类
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
	public static Object  parse(byte[] param, Class cls) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, UnsupportedEncodingException, IllegalArgumentException, InvocationTargetException {
		Object obj = null;
		if(param!=null){
			obj = cls.newInstance(); // 实例化一个返回值对象
			//获取注解排序字段列表
			List<Field> msgFields =getSortMsgFields(obj);
			int srcPos = 0;
			for (Field field : msgFields) {
				String fieldName = field.getName(); // 字段名
				Class type = field.getType(); // 字段类型
				String typeName = type.getSimpleName();// 获得字段类型名称
				String methodName = "set"+ fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
				Method method = cls.getDeclaredMethod(methodName,type);// 获取Set方法
				// 从Byte中取得值对应值
				ParamField mar = field.getAnnotation(ParamField.class);// 获得字段注解
				int len = mar.len(); // 字段对应报文长度
				Encode encode = mar.encode();// 编码格式
				Charset charset=mar.charset();//ASC编码格式
				LenType lenType=mar.lenType();//长度类型
				byte[] tempMsg=null;
				int offset=0;
				if((srcPos+len)<=param.length){//判断长度是否超出数据长度
					if(lenType==LenType.FIXED){//定长类处理
						tempMsg=new byte[len];
						offset=len;
					}else{//动态长度类处理
						byte[] lenByte=new byte[len];
						System.arraycopy(param, srcPos, lenByte, 0, lenByte.length);
						srcPos+=len;
						offset=StringUtil.bytesToInt(lenByte);
						tempMsg = new byte[offset]; // 分配空间
					}
					if((srcPos+offset)>param.length){//判断长度是否超出数据长度
						break;
					}
					System.arraycopy(param, srcPos, tempMsg, 0, tempMsg.length);
					Object value=parseByte(typeName, tempMsg, encode,charset);
					srcPos+=offset;
					method.invoke(obj, value);// 执行Set方法
				}else{
					break;
				}
			}
		}
		return obj;
	}

	/**
	 * 解析
	 * 
	 * @param typeName 参数类型
	 * @param tempMsg  数据
	 * @param encode 编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static Object parseByte(String typeName, byte[] tempMsg,Encode encode,Charset charset) throws UnsupportedEncodingException {
		Object value = null;
		Object tempValue = decode(tempMsg, encode,charset);
		// 数据类型强制转换(使得与Pojo中所定义类型吻合)
		if (typeName.equals("int")) {
			value = Integer.parseInt(tempValue.toString());
		} else if (typeName.equals("String")) {
			value = String.valueOf(tempValue);
		} else if (typeName.equals("float")) {
			value = Float.parseFloat(tempValue.toString());
		} else if (typeName.equals("double")) {
			value = Double.parseDouble(tempValue.toString());
		} else if (typeName.equals("long")) {
			value = Long.parseLong(tempValue.toString());
		} else if (typeName.equals("boolean")) {
			value = Boolean.parseBoolean(tempValue.toString());
		} else if (typeName.equals("short")) {
			value = Short.parseShort(tempValue.toString());
		} else {
			value = String.valueOf(value);
		}
		return value;
	}

	/**
	 * 编码
	 * @param b    数据
	 * @param encode  编码类型
	 * @param charset 编码格式
	 * @return
	 * @throws UnsupportedEncodingException
     */
	private static Object decode(byte[] b, Encode encode,Charset charset) throws UnsupportedEncodingException {
		Object result = null;
		switch (encode) {
		case ASC:
			String charsetName=charset.value();
			if(charset==Charset.DYNAMIC){
				charsetName= AppConstant.CODED_FORMAT;
			}
			result = new String(b,charsetName);
			break;
		case BCD:
			result = new Integer(StringUtil.bytesToInt(b));
			break;
		case HEX:
			result = StringUtil.bytesToHex(b);
			break;
		default:
			break;
		}
		return result;
	}
	
}
