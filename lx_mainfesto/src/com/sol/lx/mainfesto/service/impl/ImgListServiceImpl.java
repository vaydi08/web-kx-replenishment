package com.sol.lx.mainfesto.service.impl;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sol.lx.mainfesto.dao.BaseDao;
import com.sol.lx.mainfesto.dao.ImgListDao;
import com.sol.lx.mainfesto.dao.pojo.ImgList;
import com.sol.lx.mainfesto.service.ImgListService;

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
	
	
	@Value("${fontname}")
	private String fontName;
	@Value("${font_x}")
	private int font_x;
	@Value("${font_y}")
	private int font_y;
	@Value("${fontsize}")
	private int fontSize;
	
	public boolean pressText(String[] pressText, File srcImgFile,int color) {
		
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
			Font font = new Font(fontName, Font.BOLD, fontSize);
			g.setFont(font);
			
			int y = font_y;
			if(pressText != null && pressText.length > 0) {
				for(String text : pressText) {
					g.drawString(text, font_x, y);
					y += fontSize;
				}
			}
			
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
