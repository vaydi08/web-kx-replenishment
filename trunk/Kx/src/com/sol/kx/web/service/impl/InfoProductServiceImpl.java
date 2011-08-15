package com.sol.kx.web.service.impl;

import java.util.Arrays;

import org.sol.util.c3p0.Condition;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.service.InfoProductService;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class InfoProductServiceImpl extends BaseServiceImpl implements InfoProductService{
	/* (non-Javadoc)
	 * @see com.sol.kx.web.service.impl.InfoProductService#findProducts(com.sol.kx.web.service.bean.PagerBean, org.sol.util.c3p0.Condition)
	 */
	public PagerBean<InfoProduct> findProducts(PagerBean<InfoProduct> bean,Condition condition) {
		Logger.SERVICE.ldebug("查询产品[info_product]数据",bean.getPage(),bean.getPageSize(),
				condition == null ? "无where" : condition.getWhere(),
				condition == null ? "" : Arrays.deepToString(condition.getParams()));
		try {
			return setBeanValue(bean, 
					infoProductDao.findProducts(bean.getPage(), bean.getPageSize(), condition), 
					infoProductDao.findProductsCount(condition));
		} catch (Exception e) {
			bean.setException(e);
			exceptionHandler.onDatabaseException("查询产品[info_product]时的错误", e);
			return bean;
		}
	}
}
