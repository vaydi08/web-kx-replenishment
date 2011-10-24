package com.sol.kx.web.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class UserAuthFilter implements Filter{

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest sreq, ServletResponse res,
			FilterChain fc) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)sreq;
		String path = req.getContextPath();
		
		System.out.println(req.getRequestURI());
		
		fc.doFilter(req,res);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
