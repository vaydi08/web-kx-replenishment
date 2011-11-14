package com.sol.kx.web.action.info;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoCategory;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.InfoCategoryService;
import com.sol.kx.web.service.bean.ComboBoxBean;

@Controller
@Scope("session")
@Results({@Result(name = "choose",location = "/stock/chooseData.jsp"),
	@Result(name = "panelSelect",location = "/panelSelectData.jsp")})
public class InfoCategoryAction extends BaseAction<InfoCategory>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private InfoCategoryService infoCategoryService;
	
	@Override
	protected BaseService<InfoCategory> getService() {
		return infoCategoryService;
	}
	
	@Override
	protected InfoCategory newPojo() {
		return new InfoCategory();
	}
	
	private Integer type1;
	private Integer type2;
	private Integer type3;
	
	@Override
	public String manager() {
		InfoCategory obj = defaultQuery();
		if(type1 != null && type1 > 0)
			obj.setParent(type1);
		if(type2 != null && type2 > 0)
			obj.setParent(type2);
		if(type3 != null && type3 > 0)
			obj.setParent(type3);
		pagerBean = infoCategoryService.findCustom(pagerBean, obj);
		return DATA;
	}
	
	// 添加
	private String picData;
	@Override
	public String add() {
		if(picData != null && !picData.isEmpty())
			infoCategoryService.saveUploadPic(picData, input);
		return super.add();
	}
	
	private Integer clevel;
	private Integer parent;
	
	private static final Map<Integer,String> defaultMap = new HashMap<Integer, String>();
	static {
		defaultMap.put(1, "- 大类 -");
		defaultMap.put(2, "- 小类 -");
		defaultMap.put(3, "- 货型 -");
		defaultMap.put(4, "- 款式 -");
	}
	
	private ComboBoxBean comboboxBean;
	
	public String combobox() {
		comboboxBean = infoCategoryService.findCategoryType(clevel,parent,defaultMap.get(clevel));
		return COMBOBOX;
	}
	
	public String combobox1() {
		comboboxBean = infoCategoryService.findCategoryType1(clevel,defaultMap.get(clevel));
		return COMBOBOX;
	}

	public ComboBoxBean getComboboxBean() {
		return comboboxBean;
	}
	
	public String findType4Select() {
		pagerBean = infoCategoryService.find2(input);
		return "panelSelect";
	}
	
	public String findChoose() {
		return "choose";
	}
	public Map<String,Integer> getChoose() {
		return infoCategoryService.findCategoryChoose(input);
	}

	public void setClevel(Integer clevel) {
		this.clevel = clevel;
	}

	public void setPicData(String picData) {
		this.picData = picData;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public void setParent(String parent) {
		this.parent = Integer.parseInt(parent);
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

}
