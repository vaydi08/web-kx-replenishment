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
	
	<script src="../script/leftmenu.js"></script>
	<script src="../script/default.js"></script>

	
	<script>
	$(document).ready(function() {

	});
	</script>
	
  </head>
  
  <body>
    <sol:top />
    
	<div id="mainFrame" class="clearfix">
	<!-- 左侧菜单 -->
	<%@include file="../menu.inc" %>
	
	
	<div id="right">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td>大类</td>
		<td><td><s:select list="categoryLevel1" listKey="id" listValue="name" name="category1" theme="simple"/></td></td>
		</tr>
		<tr>
		<td>小类</td>
		<td><td><s:select list="categoryLevel2" listKey="id" listValue="name" name="category2" theme="simple"/></td></td>
		</tr>
		<tr>
		<td>货型1</td>
		<td><td><s:select list="categoryLevel3" listKey="id" listValue="name" name="category3" theme="simple"/></td></td>
		</tr>
		<tr>
		<td>货型2</td>
		<td><td><s:select list="categoryLevel4" listKey="id" listValue="name" name="category4" theme="simple"/></td></td>
		</tr>
		</table>
	</div>
	
	<!-- MAIN END -->
	</div>
  </body>
</html>
