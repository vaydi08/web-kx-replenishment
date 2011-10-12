package com.sol.kx.web.service;

import com.sol.kx.web.dao.pojo.OrderSupplier;
import com.sol.kx.web.service.bean.ComboBoxBean;

public interface OrderSupplierService extends BaseService<OrderSupplier>{
	public ComboBoxBean findSupplierCombobox();
}
