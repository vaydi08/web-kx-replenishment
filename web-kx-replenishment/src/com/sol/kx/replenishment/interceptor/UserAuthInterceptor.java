package com.sol.kx.replenishment.interceptor;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sol.kx.replenishment.common.Constants;
import com.sol.kx.replenishment.dao.pojo.SysUser;

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

		Map<String,Object> session = invocation.getInvocationContext().getSession();
		if(session.get(Constants.SESSION.user) == null) {
			SysUser user = new SysUser();
			user.setUsername("beiouwolf");
			session.put(Constants.SESSION.user, user);
		}
		return invocation.invoke();
	}

}
