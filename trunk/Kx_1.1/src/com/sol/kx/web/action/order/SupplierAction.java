package com.sol.kx.web.action.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.OrderSupplier;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.order.OrderSupplierService;

@Controller
@Scope("session")
public class SupplierAction extends BaseAction<OrderSupplier>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private OrderSupplierService orderSupplierService;
	
	public String combo() {
		comboboxBean = orderSupplierService.supplierCombobox();
		return COMBOBOX;
	}
	
	@Override
	protected OrderSupplier newPojo() {
		return new OrderSupplier();
	}

	@Override
	protected BaseService injectService() {
		return orderSupplierService;
	}

}
