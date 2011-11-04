package com.sol.lx.mainfesto.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sol.lx.mainfesto.dao.BaseDao;
import com.sol.lx.mainfesto.dao.ImgListDao;
import com.sol.lx.mainfesto.dao.pojo.ImgList;
import com.sol.lx.mainfesto.service.ImgListService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@Service
public class ImgListServiceImpl extends BaseServiceImpl<ImgList> implements ImgListService {
	
	@Autowired
	private ImgListDao imgListDao;
	
	// 图片列表
	public List<ImgList> find() {
		try {
			return imgListDao.find();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean insert(String image,String text) {
		try {
			return imgListDao.insert(image,text) == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public void compress(File tempImg,String savePath) {
		File dest = new File(savePath,tempImg.getName());
		FileOutputStream out = null;
		
		Image srcImg = null;
		try {
			srcImg = ImageIO.read(tempImg);
			int srcImgWidth = 400;
			int srcImgHeight = 300;
			
			BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight,
					BufferedImage.TYPE_INT_RGB);
			bufImg.getGraphics().drawImage(srcImg, 0,0,srcImgWidth,srcImgHeight,null);
			
			out = new FileOutputStream(dest);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(bufImg);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
		
	}
	
	private static Map<TextAttribute,Object> fontAttr;
	static {
		fontAttr = new HashMap<TextAttribute, Object>();
		fontAttr.put(TextAttribute.FAMILY,"黑体");
		fontAttr.put(TextAttribute.WEIGHT,3.0f);
		fontAttr.put(TextAttribute.SIZE,50);
	}
	private static Map<TextAttribute,Object> fontAttrText1;
	static {
		fontAttrText1 = new HashMap<TextAttribute, Object>();
		fontAttrText1.put(TextAttribute.FAMILY,"黑体");
		fontAttrText1.put(TextAttribute.WEIGHT,3.0f);
		fontAttrText1.put(TextAttribute.SIZE,70);
	}
	public boolean pressText(String text1,String text2, File srcImgFile,Color color,int x,int y) {
		
		File dest = new File(srcImgFile.getParent(), srcImgFile.getName() + ".jpg");
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(dest);
		
			// 读取原图片信息
			Image srcImg = ImageIO.read(srcImgFile);
			int srcImgWidth = srcImg.getWidth(null);
			int srcImgHeight = srcImg.getHeight(null);
			// 加水印
			BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = bufImg.createGraphics();
			g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
			Font font = new Font(fontAttr);
			Font fontText1 = new Font(fontAttrText1);
			
			g.setFont(font);
			g.setColor(color);
			
			g.drawString("光棍最", x, y);
			y += fontText1.getSize();
			g.setFont(fontText1);
			g.drawString(text1, x, y);
			g.setFont(font);
			y += font.getSize();
			g.drawString("因为是" + text2, x, y);
			
			g.dispose();
			// 输出图片
			ImageIO.write(bufImg, "jpg", out);
			out.flush();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
	}
	
	@Override
	protected BaseDao getDao() {
		return imgListDao;
	}

}
