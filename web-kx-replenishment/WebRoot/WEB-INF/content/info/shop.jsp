<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sol" uri="/sol-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link href="../images/style.css" rel="stylesheet" type="text/css" />
	<script src="../script/jquery/jquery.js"></script>
	
	
	<script>
	$(document).ready(function() {

	});
	</script>
	
  </head>
  
  <body>
	
	<div id="right" class="input">

		<table border="0" cellpadding="0" cellspacing="0" class="input">
		<tr>
		<td width="70" height="30">门店名称</td>
		<td width="230"><s:textfield name="input.name" theme="simple"/></td>
		</tr>
		<tr>
		<td width="70" >门店代码</td>
		<td width="230"><s:textfield name="input.pcode" theme="simple"/></td>
		</tr>
		<tr>
		<td width="70" height="30">地址</td>
		<td><s:textfield name="input.quality" theme="simple"/></td>
		</tr>
		<tr>
		<td>联系电话</td>
		<td><s:textfield name="input.weight" theme="simple"/></td>
		</tr>
		<tr>
		<td width="70" height="30">备注</td>
		<td><s:textfield name="input.stand" theme="simple"/></td>
		</tr>
		
		<tr>
		<td colspan="2" align="center" height="30"><img src="../images/submit.png"/></td>
		</tr>
		
		</table>
	</div>

  </body>
</html>
