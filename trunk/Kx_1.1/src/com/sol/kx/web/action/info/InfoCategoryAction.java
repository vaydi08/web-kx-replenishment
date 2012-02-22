package com.sol.kx.web.action.info;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoCategory;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.ComboBoxBean;
import com.sol.kx.web.service.info.InfoCategoryService;

@Controller
@Scope("session")
public class InfoCategoryAction extends BaseAction<InfoCategory>{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private InfoCategoryService infoCategoryService;
	
	private Integer type1;
	private Integer type2;
	private Integer type3;
	
	public String manager() {
		InfoCategory obj = defaultQuery();
		if(type1 != null && type1 > 0)
			obj.setParent(type1);
		if(type2 != null && type2 > 0)
			obj.setParent(type2);
		if(type3 != null && type3 > 0)
			obj.setParent(type3);
		infoCategoryService.selectByPage(pagerBean, obj);
		return JSONDATA;
	}
	
	public String delete() {
		result = infoCategoryService.deleteCheck(input);
		return RESULT;
	}
	
	private String picData;
	private String picType;
	private File picFile;
	
	@Override
	public String add() {
		if(picType.equals("webcap") && picData != null && !picData.isEmpty())
			result = infoCategoryService.saveUploadPic(picData,input);
		else if(picType.equals("file") && picFile != null)
			result = infoCategoryService.saveUploadPic(picFile,input);
		else if(input.getClevel() < 4)
			result = infoCategoryService.insert(input);
			
		return RESULT;
	}
	
	public String checkCode() {
		result = infoCategoryService.checkExists(input);
		return RESULT;
	}
		
	private static final Map<Integer,String> defaultMap = new HashMap<Integer, String>();
	static {
		defaultMap.put(1, "- 大类 -");
		defaultMap.put(2, "- 小类 -");
		defaultMap.put(3, "- 货型 -");
	}
	
	public String typeQueryCombo() {
		comboboxBean = infoCategoryService.findCategoryType(input,defaultMap.get(input.getClevel()));
		return COMBOBOX;
	}
	
	public String newTypeCombo() {
		comboboxBean = infoCategoryService.findCategoryTypeByNew(input);
		return COMBOBOX;
	}
	
	@Override
	protected InfoCategory newPojo() {
		return new InfoCategory();
	}

	@Override
	protected BaseService injectService() {
		return infoCategoryService;
	}

	public void setType1(Integer type1) {
		this.type1 = type1;
	}

	public void setType2(Integer type2) {
		this.type2 = type2;
	}

	public void setType3(Integer type3) {
		this.type3 = type3;
	}

	public void setPicData(String picData) {
		this.picData = picData;
	}

	public void setPicType(String picType) {
		this.picType = picType;
	}

	public void setPicFile(File picFile) {
		this.picFile = picFile;
	}
}
