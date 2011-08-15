package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import org.sol.util.c3p0.Condition;

import com.sol.kx.web.dao.pojo.InfoProduct;

public interface InfoProductDao {

	/**
	 * 查询产品
	 * @param page
	 * @param pageSize
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<InfoProduct> findProducts(int page, int pageSize,
			Condition condition) throws Exception;

	/**
	 * 查询产品统计
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public int findProductsCount(Condition condition) throws SQLException;

}