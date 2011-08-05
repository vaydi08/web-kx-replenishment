package org.sol.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableUtil {
	/**
	 * 对象序列化
	 * @param o 序列化对象
	 * @param docname 文件夹名
	 * @param filename 文件名
	 * @throws IOException
	 */
	public static void serialize(Object o,String docname,String filename) throws IOException {
		// 输出目录
		File doc = new File(docname);
		if(!doc.exists())
			doc.mkdirs();
		
		// 输出文件
		File file = new File(doc,filename);
		// 输出文件流
		FileOutputStream fos = new FileOutputStream(file);
		// 对象包装流
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		// 写序列化文件
		oos.writeObject(o);
		oos.close();
	}
	
	/**
	 * 反序列化对象
	 * @param docname 文件夹名
	 * @param filename 文件名
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deSerialize(String docname,String filename) throws IOException, ClassNotFoundException {
		// 输出目录
		File doc = new File(docname);
		// 输出文件
		File file = new File(doc,filename);
		// 输出文件流
		FileInputStream fis = new FileInputStream(file);
		// 对象包装流
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		return ois.readObject();
	}
}
