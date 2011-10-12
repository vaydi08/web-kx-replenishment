package com.sol.kx.web.service.impl;

import java.util.List;

import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.OrderSupplierDao;
import com.sol.kx.web.dao.pojo.OrderSupplier;
import com.sol.kx.web.service.OrderSupplierService;
import com.sol.kx.web.service.bean.ComboBoxBean;

@Service
public class OrderSupplierServiceImpl extends BaseServiceImpl<OrderSupplier> implements OrderSupplierService{

	@Autowired
	private OrderSupplierDao orderSupplierDao;
	
	public ComboBoxBean findSupplierCombobox() {
		Logger.SERVICE.debug("查询[order_supplier]数据,转换为Combobox");
		SelectEntity entity = new SelectEntity();
		
		try {
			entity.init(new OrderSupplier());
			List<OrderSupplier> list = orderSupplierDao.find2(entity);
			ComboBoxBean bean = new ComboBoxBean();
			for(OrderSupplier orderSupplier : list)
				bean.addData(orderSupplier.getName(), orderSupplier.getId(), orderSupplier.getContact());
			bean.setFirstSelect();
			return bean;
		} catch (Exception e) {
			Logger.SERVICE.error("查询[order_supplier]数据,转换为Combobox错误",e);
			return new ComboBoxBean();
		}
	}
	
	@Override
	protected BaseDao getDao() {
		return orderSupplierDao;
	}

}
