package org.sol.util.textCounter; 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * 扣量随机数列表数组
 * 100个boolean 的数组,以位置比对,需要扣量的位置置true,否则置false
 * @author HUYAO
 *
 */
public class RandomData implements Serializable{
	private static final long serialVersionUID = -5978453238714683741L;

	/**
	 * 扣量比对数组
	 */
	private boolean[] random;
	
	/**
	 * 序列化文件目录
	 */
	private transient String savingDoc;
	/**
	 * 序列化文件名
	 */
	private transient String savingFilename;
	/**
	 * which_service
	 */
	private transient String which_service;
	/**
	 * 扣量指标
	 */
	private transient int discount;
	
	public RandomData(String savingDoc,String which_service,int discount) throws IOException, ClassNotFoundException {
		this.savingDoc = savingDoc;
		this.which_service = which_service;
		this.savingFilename = "randomdata" + which_service + ".dat";
		this.discount = discount;

		this.makeDocExist();
		
		initRandom();
	}
	
	/**
	 * 初始化数组
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void initRandom() throws IOException, ClassNotFoundException {
		genarateRandom();
		save();
	}
	
	/**
	 * 生成数组
	 */
	public void genarateRandom() {
		Random r = new Random();
		
		// 填充false到数组
		if(random == null)
			random = new boolean[100];
		else
			Arrays.fill(random, false);
		
		int value;
		int times = 0;
		
		if(discount == 0) return;
		
		do {
			do {
				// 生成一个随机值
				value = r.nextInt(100);
			} while(random[value]);
			// 填入数组
			random[value] = true;
		}while(++times < discount);
	}
	
	public boolean getRandom(int i) {
		return random[i];
	}
	
	public void makeDocExist() {
		File file = new File(savingDoc);
		if(!file.exists())
			file.mkdirs();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(which_service);
		sb.append(" 随机数组:");
		for(int i = 0; i < random.length; i ++) {
			if(random[i]) {
				sb.append(i);
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 序列化文件
	 * @throws IOException
	 */
	public void save() throws IOException {
		File file = new File(savingDoc,savingFilename);
		OutputStream os;
		ObjectOutputStream oos = null;

		os = new FileOutputStream(file);
		oos = new ObjectOutputStream(os); 
		oos.writeObject(this);
		oos.close();
	}
	
	/**
	 * 读取序列化文件
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void read() throws IOException, ClassNotFoundException {
		File file = new File(savingDoc,savingFilename);
		InputStream is;
		ObjectInputStream ois = null;

		is = new FileInputStream(file);
		ois = new ObjectInputStream(is);
		RandomData r = (RandomData)ois.readObject();
		ois.close();
		this.random = r.random;
	}

}
