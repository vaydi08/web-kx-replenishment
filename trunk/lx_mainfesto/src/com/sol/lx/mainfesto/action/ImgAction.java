package com.sol.lx.mainfesto.action;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.sol.lx.mainfesto.dao.pojo.ImgList;
import com.sol.lx.mainfesto.service.ImgListService;

@Controller
@Scope("session")
@Results( { @Result(name = "imgList", location = "/main.jsp"),
		@Result(name = "upload", location = "/upload.jsp") })
public class ImgAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ImgListService imgListService;

	private List<ImgList> imgList;
	private int splitLen;

	public String list() {
		imgList = imgListService.find();
		splitLen = (int) Math.ceil(imgList.size() / 3.0);
		return "imgList";
	}

	private File[] Filedata;
	private boolean success;

	public String upload() {
		String urlTemp = ServletActionContext.getServletContext().getRealPath("/temp");
//		File dest = new File(urlTemp,Filedata[0].getName());
//
//		Filedata[0].renameTo(dest);
//		ActionContext.getContext().getSession().put("tempImg", dest.getName());
		imgListService.compress(Filedata[0], urlTemp);
		file = Filedata[0].getName();
		
		success = pressText();
		return "upload";
	}

	private String text1;
	private String text2;
	private int x;
	private int y;
	private int color;
	private String file;
	
	public String textTemp() {
//		ActionContext.getContext().getSession().put("tempText", text);
		
		success = pressText();

		return "upload";
	}
	
	private boolean pressText() {
		if(file != null && !file.equals("") && text1 != null && text2 != null) {
			String urlTemp = ServletActionContext.getServletContext().getRealPath("/temp");
			File img = new File(urlTemp,file);
			Color c = (color == 1) ? Color.white : Color.black;
			return imgListService.pressText(text1,text2, img, c.getRGB(),x,y);
		} else
			return false;
	}
	
	public String submit() {
		String urlTemp = ServletActionContext.getServletContext().getRealPath("/temp");
		String urlPic = ServletActionContext.getServletContext().getRealPath("/pic");
		File temp = new File(urlTemp,file);
		File img = new File(urlTemp,file + ".jpg");
		
		file = UUID.randomUUID().toString() + ".jpg";
//		String text = (String) ActionContext.getContext().getSession().get("tempText");
		
		File dest = new File(urlPic,file);
		img.renameTo(dest);
		temp.delete();
		
		success = imgListService.insert(file, text1+text2);
		
//		ActionContext.getContext().getSession().remove("tempImg");
//		ActionContext.getContext().getSession().remove("tempText");
		return "upload";
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

	public int getSplitLen() {
		return splitLen;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getColor() {
		return color;
	}
}
