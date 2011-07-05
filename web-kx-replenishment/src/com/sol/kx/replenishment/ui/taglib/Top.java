package com.sol.kx.replenishment.ui.taglib;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.sol.kx.replenishment.common.Constants;
import com.sol.kx.replenishment.common.Util;
import com.sol.kx.replenishment.dao.pojo.SysUser;

public class Top extends BodyTagSupport{

	private static final long serialVersionUID = 1L;

	@Override
	public int doEndTag() throws JspException {
		JspWriter out = super.pageContext.getOut();
		HttpSession session = super.pageContext.getSession();
		
		SysUser user = (SysUser)session.getAttribute(Constants.SESSION.user);
		
		try {
			out.print("<div id='top'>");
			out.print("<div class='topStr'>当前用户：" + user.getUsername() + " " + Util.getDate() + " <a href=''>重新登录</a></div>");
			out.print("</div>");
		} catch (IOException e) {
			return super.doEndTag();
		}
		
		return super.doEndTag();
	}
}
