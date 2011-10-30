package com.sol.lx.mainfesto.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sol.lx.mainfesto.dao.pojo.ImgList;
import com.sol.lx.mainfesto.service.ImgListService;
import com.sun.accessibility.internal.resources.accessibility;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@Controller
@Scope("session")
@Results( { @Result(name = "imgList", location = "/imgList.jsp"),
		@Result(name = "upload", location = "/upload.jsp") })
public class ImgAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	// @Autowired
	// private ImgListService imgListService;

	private List<ImgList> imgList;

	// public String list() {
	// imgList = imgListService.find();
	//		
	// return "imgList";
	// }

	private File[] Filedata;
	private boolean success;

	public String upload() {
		System.out.println(Filedata[0]);
		File dest = new File("F:/tools/tomcat/webapps/mainfesto/temp/",
				Filedata[0].getName());
		Filedata[0].renameTo(dest);

		ActionContext.getContext().getSession().put("tempImg", dest.getName());
		success = true;
		return "upload";
	}

	public String tempImg() {
		File img = new File("F:/tools/tomcat/webapps/mainfesto/temp/",
				(String) ActionContext.getContext().getSession().get("tempImg"));

		File dest = new File(img.getParent(),img.getName() + ".jpg");
		try {
			FileOutputStream fos = new FileOutputStream(dest);
			pressText2("test", img, "黑体", Color.ORANGE.getRGB(), 30, 10, 20, fos);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ActionContext.getContext().getSession().put("waterImg", dest.getName());
		success = true;
		return "upload";
	}

	public static void pressText(String pressText, File _file, String fontName,
			int fontStyle, int color, int fontSize, int x, int y,
			OutputStream out) {
		try {
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			// 只有图片的宽或高大于200的时候才添加水印（小图片不添加）
			if (wideth > 200 || height > 200) {
				BufferedImage image = new BufferedImage(wideth, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = image.createGraphics();
				g.drawImage(src, 0, 0, wideth, height, null);
				g.setColor(new Color(color));
				g.setFont(new Font(fontName, fontStyle, fontSize));
				g.drawString(pressText, wideth - fontSize - x, height
						- fontSize / 2 - y);
				g.dispose();
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(image);
				out.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void pressText2(String pressText, File srcImgFile, String fontName,
			int color, int fontSize, int x, int y,
			OutputStream out) throws IOException {
		// 读取原图片信息
		Image srcImg = ImageIO.read(srcImgFile);
		int srcImgWidth = srcImg.getWidth(null);
		int srcImgHeight = srcImg.getHeight(null);
		// 加水印
		BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bufImg.createGraphics();
		g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
		Font font = new Font(fontName, Font.PLAIN, fontSize);
		g.setFont(font);
		g.drawString(pressText, x, y);
		g.dispose();
		// 输出图片
		ImageIO.write(bufImg, "jpg", out);
		out.flush();
		out.close();

	}

	public List<ImgList> getImgList() {
		return imgList;
	}

	public void setFiledata(File[] filedata) {
		Filedata = filedata;
	}

	public boolean isSuccess() {
		return success;
	}
}
