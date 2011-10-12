package com.sol.kx.web.action.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.OrderSupplier;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.OrderSupplierService;
import com.sol.kx.web.service.bean.ComboBoxBean;

@Controller
@Scope("session")
public class SupplierAction extends BaseAction<OrderSupplier>{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private OrderSupplierService orderSupplierService;
	
	@Override
	protected BaseService<OrderSupplier> getService() {
		return orderSupplierService;
	}

	private ComboBoxBean comboboxBean;
	
	public String suppliers() {
		comboboxBean = orderSupplierService.findSupplierCombobox();
		return COMBOBOX;
	}
	
	@Override
	protected OrderSupplier newPojo() {
		return new OrderSupplier();
	}

	public ComboBoxBean getComboboxBean() {
		return comboboxBean;
	}

}
