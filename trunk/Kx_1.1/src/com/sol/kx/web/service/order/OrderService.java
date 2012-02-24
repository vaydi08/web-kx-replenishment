package com.sol.kx.web.service.order;

import java.util.List;

import org.sol.util.mybatis.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.order.OrderFeedbackMapper;
import com.sol.kx.web.dao.order.OrderTypeMapper;
import com.sol.kx.web.dao.pojo.OrderFeedback;
import com.sol.kx.web.dao.pojo.OrderType;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;

@Service
public class OrderService extends BaseService{

	@Autowired
	private OrderTypeMapper orderTypeMapper;
	
	@Autowired
	private OrderFeedbackMapper orderFeedbackMapper;
	
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public ResultBean accecpt(OrderType obj,OrderFeedback feedback) {
		LDEBUG("更新数据 ",obj.toString(),feedback.toString());
		
		try {
			orderTypeMapper.update(obj);
			
			orderFeedbackMapper.insert(feedback);
			
			return ResultBean.RESULT_SUCCESS(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("更新错误","order_type,order_feedback", e);
			return ResultBean.RESULT_ERR(e.getMessage(), obj);
		}
	}
	
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public ResultBean repostOk(OrderType obj,OrderFeedback feedback) {
		LDEBUG("更新数据 ",obj.toString(),feedback.toString());
		
		try {
			orderTypeMapper.update(obj);
			
			orderFeedbackMapper.update(feedback);
			
			return ResultBean.RESULT_SUCCESS(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("更新错误","order_type,order_feedback", e);
			return ResultBean.RESULT_ERR(e.getMessage(), obj);
		}
	}
	
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public ResultBean repostFail(OrderType obj,OrderFeedback of1,OrderFeedback newfeedback) {
		ResultBean result = repostOk(obj,of1);
		
		DEBUG("插入[order_feedback]数据 " + newfeedback.toString());
		try {
			orderFeedbackMapper.insert(newfeedback);
			return result;
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("更新错误","order_type,order_feedback", e);
			return ResultBean.RESULT_ERR(e.getMessage(), obj);
		}
	}
	
	@Transactional(readOnly = true)
	public void selectUntake(PagerBean<OrderType> bean,OrderType obj) {
		LDEBUG("查找 [OrderType] 未处理数据",bean.getPage(),bean.getPageSize());
		
		obj.setPage(bean.getPage());
		obj.setPageSize(bean.getPageSize());
		
		bean.setDataList(orderTypeMapper.untake(obj));
		bean.setCount(orderTypeMapper.untakeCount());
	}
	
	@Transactional(readOnly = true)
	public void selectSelf(PagerBean<OrderType> bean,OrderType obj) {
		LDEBUG("查找 [OrderType] 自身数据",bean.getPage(),bean.getPageSize());
		
		obj.setPage(bean.getPage());
		obj.setPageSize(bean.getPageSize());
		
		bean.setDataList(orderTypeMapper.self(obj));
		bean.setCount(orderTypeMapper.selfCount(obj));
	}
	
	@Transactional(readOnly = true)
	public void selectAll(PagerBean<OrderType> bean,OrderType obj) {
		LDEBUG("查找 [OrderType] 全部数据",bean.getPage(),bean.getPageSize());
		
		obj.setPage(bean.getPage());
		obj.setPageSize(bean.getPageSize());
		
		bean.setDataList(orderTypeMapper.all(obj));
		bean.setCount(orderTypeMapper.allCount());
	}
	
	@Transactional(readOnly = true)
	public OrderType get(OrderType obj) {
		DEBUG("获取 [OrderFeedback] 数据 id:" + obj.getId());
		
		return orderTypeMapper.get(obj);
	}
	
	@Transactional(readOnly = true)
	public List<OrderFeedback> selectFeedback(OrderFeedback obj) {
		DEBUG("查找 [OrderFeedback] 数据");
		
		return orderFeedbackMapper.select(obj);
	}
	
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public ResultBean updateFeedback(OrderFeedback obj) {
		DEBUG("更新 [OrderFeedback] 数据");
		
		try {
			orderFeedbackMapper.update(obj);
			return ResultBean.RESULT_SUCCESS(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("更新错误","order_feedback", e);
			return ResultBean.RESULT_ERR(e.getMessage(), obj);
		}
	}
	
	@Override
	protected BaseMapper injectMapper() {
		return orderTypeMapper;
	}

}
