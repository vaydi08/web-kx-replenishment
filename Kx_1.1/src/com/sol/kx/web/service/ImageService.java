package com.sol.kx.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Decoder;


@Service
public class ImageService {

	@Value("${image.path}")
	private String image_path;
	
	public InputStream load(String img) {
		try {
			return new FileInputStream(new File(image_path,img));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public void save(String picData,String filename) throws IOException {
		OutputStream fos = null;
		try {
			BASE64Decoder decode=new BASE64Decoder();
			byte[] datas=decode.decodeBuffer(picData);
			
			File file=new File(image_path,filename);
			fos=new FileOutputStream(file);
			fos.write(datas);
			fos.flush();
		} finally {
			if(fos != null)
				try {
					fos.close();
				} catch (IOException e) {
				}
		}
	}
	
	public void save(File img,String filename) throws IOException {
		OutputStream fos = null;
		InputStream fis = null;
		try {
			File file=new File(image_path,filename);
			fis = new FileInputStream(img);
			fos=new FileOutputStream(file);
			
			byte[] buf = new byte[2048];
			int size = 0;
			while((size = fis.read(buf)) != -1)
				fos.write(buf,0,size);
			fos.flush();
		} finally {
			if(fos != null)
				fos.close();
			if(fis != null)
				fis.close();
		}
	}
}
