package com.itep.mt.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParamField{

	int    sort()   default 0;           //字段排序

	int    len()    default 0;           //报文长度

	LenType lenType() default LenType.FIXED; //长度类型

	Charset charset() default Charset.DYNAMIC;

	Encode encode() default Encode.ASC;  //编码格式

	public enum Encode{
		ASC,   //ASCII编码
		BCD,   //二进码十进数
		HEX    //十六进制码
	}

	public enum Charset{
		GBK("GBK"),   //GBK

		UTF_8("UTF-8"),   //UTF_8

		DYNAMIC("dynamic");   //动态编码

		private String charset;

		private Charset(String charset) {
			this.charset=charset;
		}

		public String value() {
			return this.charset;
		}
	}

	public enum LenType{
		FIXED,   //固定
		DYNAMIC    //动态
	}

}
