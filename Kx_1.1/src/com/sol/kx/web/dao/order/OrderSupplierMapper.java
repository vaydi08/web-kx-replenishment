package com.sol.kx.web.dao.order;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;
import org.sol.util.mybatis.BaseMapper;
import org.sol.util.mybatis.SelectTemplate;

import com.sol.kx.web.dao.pojo.OrderSupplier;

public interface OrderSupplierMapper extends BaseMapper<OrderSupplier>{

	@SelectProvider(type = SelectTemplate.class,method = "select")
	public List<OrderSupplier> select(OrderSupplier obj);
}
