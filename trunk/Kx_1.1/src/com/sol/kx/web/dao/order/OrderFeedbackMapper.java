package com.sol.kx.web.dao.order;

import java.util.List;

import org.sol.util.mybatis.BaseMapper;
import com.sol.kx.web.dao.pojo.OrderFeedback;

public interface OrderFeedbackMapper extends BaseMapper<OrderFeedback>{

	public List<OrderFeedback> select(OrderFeedback obj);
}
