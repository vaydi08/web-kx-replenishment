package com.sol.kx.web.action.info;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoCategory;
import com.sol.kx.web.service.bean.ComboBoxBean;

@Controller
@Scope("session")
public class InfoCategoryAction extends BaseAction<InfoCategory>{

	private static final long serialVersionUID = 1L;

	private static final Map<Integer,String> defaultMap = new HashMap<Integer, String>();
	static {
		defaultMap.put(1, "- 大类 -");
		defaultMap.put(2, "- 小类 -");
		defaultMap.put(3, "- 货型1 -");
		defaultMap.put(4, "- 货型2 -");
	}
	private int clevel;
	
	
	public String combobox() {
		return COMBOBOX;
	}

	public ComboBoxBean getComboboxBean() {
		return infoCategoryService.findCategoryType(clevel,defaultMap.get(clevel));
	}

	public void setClevel(int clevel) {
		this.clevel = clevel;
	}

}
