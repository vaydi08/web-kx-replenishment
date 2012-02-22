package com.sol.kx.web.service.stock;

import org.sol.util.mybatis.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.dao.pojo.StockCheckSum;
import com.sol.kx.web.dao.stock.StockCheckMapper;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.ResultBean;

@Service
public class StockCheckService extends BaseService {
	@Autowired
	private StockCheckMapper stockCheckMapper;
	
	public StockCheckSum stockCheckSum(StockCheck obj) {
		DEBUG("查询数据 StockCheck合计数");
		
		return stockCheckMapper.stockCheckSum(obj);
	}
	
	// 核定数 复制
	public ResultBean copyCheck(StockCheck obj) {
		DEBUG("复制[stock_check]核定数 " + obj.toString());
		
		try {
			stockCheckMapper.copyCheck(obj);
			return ResultBean.RESULT_SUCCESS(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("复制[stock_check]核定数 " + obj.toString(),"stock_check", e);
			return ResultBean.RESULT_ERR(e.getMessage(), obj);
		}
	}
	
	@Override
	protected BaseMapper injectMapper() {
		return stockCheckMapper;
	}

}
