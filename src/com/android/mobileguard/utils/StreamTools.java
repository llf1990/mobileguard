package com.android.mobileguard.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
	/**
	 * ��ȡһ����������ת�����ַ���
	 * @throws IOException 
	 */
	
	public static String readStream(InputStream is) throws IOException{
		byte[] buffer = new byte[2014];
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		int len = 0 ;
		while((len = is.read(buffer)) != -1){
			bao.write(buffer, 0, len);
		}
		return bao.toString();
	}
}
