<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>top.html</title>
	
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
    <link href="images/style.css" rel="stylesheet" type="text/css" />

  </head>
  
  <body>
  	<s:bean name="java.util.Date" var="now" />
    <div id='top'><div class='topStr'>当前用户：<s:property value="#session[@com.sol.kx.web.common.Constants@SESSION_USER]" /> <s:date name="#now" format="yyyy / MM / dd"/> <a href='sys!logout.action'>退出登录</a> <a href="sys!main.action" target="mainFrame">首页</a></div></div>
  </body>
</html>
