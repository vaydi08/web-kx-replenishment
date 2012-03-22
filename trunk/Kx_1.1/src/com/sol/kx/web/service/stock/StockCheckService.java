package com.sol.kx.web.service.stock;

import org.sol.util.mybatis.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.dao.pojo.StockCheckSum;
import com.sol.kx.web.dao.pojo.StockChecked;
import com.sol.kx.web.dao.stock.StockCheckMapper;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;

@Service
public class StockCheckService extends BaseService {
	@Autowired
	private StockCheckMapper stockCheckMapper;
	
	@Transactional(readOnly = true)
	public StockCheckSum stockCheckSum(StockCheck obj) {
		DEBUG("查询数据 StockCheck合计数");
		
		return stockCheckMapper.stockCheckSum(obj);
	}
	
	// 核定数 复制
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
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
	
	@Transactional(readOnly = true)
	public void selectChecked(PagerBean<StockChecked> bean,StockChecked obj) {
		DEBUG("查找[stock_check]已经核定数 " + obj.toString());
		
		bean.setDataList(stockCheckMapper.selectChecked(obj));
	}
	
	@Transactional(readOnly = true)
	public void selectCheckedDetail(PagerBean<StockCheck> bean,StockCheck obj) {
		DEBUG("查找[stock_check]已经核定明细 " + obj.toString());
		
		bean.setDataList(stockCheckMapper.selectCheckedDetail(obj));
	}
	
	@Override
	protected BaseMapper injectMapper() {
		return stockCheckMapper;
	}

}
