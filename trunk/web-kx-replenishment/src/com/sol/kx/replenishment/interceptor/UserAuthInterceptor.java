package com.sol.kx.replenishment.interceptor;

import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@Component
public class UserAuthInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
//		Map<String,Object> session = invocation.getInvocationContext().getSession();
//		MsrvUser user = (MsrvUser) session.get(Constants.SKEY_USER);
//		
//		if(user == null)
//			return Action.LOGIN;
//		else
//			return invocation.invoke();
		return invocation.invoke();
	}

}
