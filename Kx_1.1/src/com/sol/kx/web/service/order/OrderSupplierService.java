package com.sol.kx.web.service.order;

import java.util.List;

import org.sol.util.mybatis.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.web.dao.order.OrderSupplierMapper;
import com.sol.kx.web.dao.pojo.OrderSupplier;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.ComboBoxBean;

@Service
public class OrderSupplierService extends BaseService{

	@Autowired
	private OrderSupplierMapper orderSupplierMapper;
	
	@Transactional(readOnly = true)
	public ComboBoxBean supplierCombobox() {
		DEBUG("查询[order_supplier]数据,转换为Combobox");
		
		try {
			List<OrderSupplier> list = orderSupplierMapper.select(new OrderSupplier());
			ComboBoxBean bean = new ComboBoxBean();
			for(OrderSupplier orderSupplier : list)
				bean.addData(orderSupplier.getName(), orderSupplier.getId(), orderSupplier.getContact());
			bean.setFirstSelect();
			return bean;
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询[order_supplier]数据,转换为Combobox错误","order_supplier",e);
			return new ComboBoxBean();
		}
	}
	
	@Override
	protected BaseMapper injectMapper() {
		return orderSupplierMapper;
	}

}
