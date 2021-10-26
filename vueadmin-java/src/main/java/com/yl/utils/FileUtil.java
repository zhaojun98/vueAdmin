package com.yl.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

	public static final String ROOT = "upload";
	
	//获取后缀名
	public static  String getSuffix(String fileName){
		int position = fileName.lastIndexOf(".");
		if(position>0){
		   return fileName.substring(position);
		}
		return fileName;
	}

	public static byte[] getFileByte(InputStream inputStream){
		ByteArrayOutputStream bos= null;
		BufferedInputStream in = null;
		try {
			bos= new ByteArrayOutputStream();
			in = new BufferedInputStream(inputStream);
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		}catch (IOException e){
            e.printStackTrace();
		}finally {
			try{
				if(bos!=null){
					bos.close();
				}
				if(in!=null){
					in.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

}
