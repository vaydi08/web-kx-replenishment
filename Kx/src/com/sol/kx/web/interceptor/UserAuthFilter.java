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
import org.sol.util.common.ContextUtil;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.SysAuthService;

public class UserAuthFilter implements Filter{

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest sreq, ServletResponse sres,
			FilterChain fc) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)sreq;
		HttpServletResponse res = (HttpServletResponse)sres;
		
		SysUser user = (SysUser) req.getSession().getAttribute(Constants.SESSION_USER);
		
		if(user == null) {
			PrintWriter out = null;
			try {
				out = res.getWriter();
				out.print("<html><head><script>top.location.href='" + req.getContextPath() + "/login.html'</script></head><body></body></html>");
				out.flush();
			} finally {
				out.close();
			}
			return;
		}
		
		if(sysAuthService.checkAuth(req.getRequestURI(), user))
			fc.doFilter(req,res);
		else
			req.getRequestDispatcher("/403.html").forward(req, sres);
	}

	private SysAuthService sysAuthService;
	
	public void init(FilterConfig arg0) throws ServletException {
		sysAuthService = ContextUtil.getObject(SysAuthService.class);
	}

}
