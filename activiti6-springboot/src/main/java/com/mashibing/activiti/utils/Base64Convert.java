package com.mashibing.activiti.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author:孙志强
 * @date 2020/09/30 14:09
 * @Modified BY:
 **/

public class Base64Convert {
//	BASE64Decoder decoder = new BASE64Decoder();

	public static String ioToBase64(InputStream inputStream) throws IOException {
		String strBase64 = null;
		// in.available()返回文件的字节长度
		byte[] bytes = new byte[inputStream.available()];
		// 将文件中的内容读入到数组中
		inputStream.read(bytes);
		strBase64 = new BASE64Encoder().encode(bytes);
		inputStream.close();
		return strBase64;
	}

	public static InputStream base64ToIo(String strBase64) throws IOException {
		// 解码，然后将字节转换为流
		byte[] bytes = new BASE64Decoder().decodeBuffer(strBase64);
		InputStream in = new ByteArrayInputStream(bytes);
		return in;
	}
}
