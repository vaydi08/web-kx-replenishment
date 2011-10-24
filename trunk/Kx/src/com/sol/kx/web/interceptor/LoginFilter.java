package com.sol.kx.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.SysUser;

public class LoginFilter implements Filter{

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest sreq, ServletResponse sres,
			FilterChain fc) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)sreq;

		HttpSession session = (HttpSession)req.getSession();
		SysUser user = (SysUser) session.getAttribute(Constants.SESSION_USER);
		
		if(user == null) {
			HttpServletResponse res = (HttpServletResponse)sres;
			PrintWriter out = res.getWriter();
			out.print("<html><head><script>top.location.href='" + req.getContextPath() + "/login.html'</script></head><body></body></html>");
			out.flush();
			out.close();
		} else
			fc.doFilter(req,sres);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
