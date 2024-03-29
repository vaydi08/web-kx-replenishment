package com.sol.kx.web.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.ResultBean;
import com.sol.kx.web.service.sys.SysService;

@Controller
@Scope("session")
@Results({@Result(name = "loginOk",location = "/LoginResult.jsp"),
		  @Result(name = "loginFail",location = "/Login.jsp"),
		  @Result(name = "main",location = "/main.jsp"),
		  @Result(name = "index",type="redirect",location = "/html/main.html")})
public class SysAction extends BaseAction<SysUser> {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SysService sysService;
	
	public String manager() {
		sysService.selectByPage(pagerBean);
		return JSONDATA;
	}
	
	public String login() {
		SysUser user = sysService.login(input);
		if(user != null) {
			ActionContext.getContext().getSession().put(Constants.SESSION_USER, user);
			result = ResultBean.RESULT_SUCCESS(user);
		} else {
			result = ResultBean.RESULT_ERR("用户名 / 密码有误,请重新尝试登录",input);
		}
		return RESULT;
	}
	
	public String index() {
		return "index";
	}
	
	public String main() {
		return "main";
	}
	
	public void logout() {
		ActionContext.getContext().getSession().remove(Constants.SESSION_USER);
		
		PrintWriter out = null;
		try {
			HttpServletRequest req = (HttpServletRequest)ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
			out = ((HttpServletResponse)ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE)).getWriter();
			out.print("<html><head><script>top.location.href='" + req.getContextPath() + "/login.html'</script></head><body></body></html>");
			out.flush();
		} catch (IOException e) {
		} finally {
			out.close();
		}
	}

	@Override
	protected SysUser newPojo() {
		return new SysUser();
	}

	@Override
	protected BaseService injectService() {
		return sysService;
	}

}
