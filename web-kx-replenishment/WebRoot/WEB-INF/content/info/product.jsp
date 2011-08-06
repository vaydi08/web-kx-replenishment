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
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td width="70" height="30">大类</td>
		<td width="230"><s:select list="categoryLevel1" listKey="id" listValue="name" name="category1" theme="simple"/></td>
		<td width="70">小类</td>
		<td width="230"><s:select list="categoryLevel2" listKey="id" listValue="name" name="category2" theme="simple"/></td>

		<td><a href="category!preNew.action" id="newCategory">增加新分类</a></td>
		</tr>
		
		<tr>
		<td>货型1</td>
		<td><s:select list="categoryLevel3" listKey="id" listValue="name" name="category3" theme="simple"/></td>
		<td>货型2</td>
		<td><s:select list="categoryLevel4" listKey="id" listValue="name" name="category4" theme="simple"/></td>
		
		<td></td>
		</tr>
		</table>
		
		<div class="clear5"></div>
		
		<table border="0" cellpadding="0" cellspacing="0" class="input">
		<tr>
		<td width="70" height="30">商品品名</td>
		<td width="230"><s:textfield name="input.name" theme="simple"/></td>
		<td width="70" >商品代码</td>
		<td width="230"><s:textfield name="input.pcode" theme="simple"/></td>
		</tr>
		<tr>
		<td width="70" height="30">金属成色</td>
		<td><s:textfield name="input.quality" theme="simple"/></td>
		<td>金重</td>
		<td><s:textfield name="input.weight" theme="simple"/></td>
		</tr>
		<tr>
		<td width="70" height="30">规格</td>
		<td><s:textfield name="input.stand" theme="simple"/></td>
		<td width="70" height="30">单位</td>
		<td><s:textfield name="input.unit" theme="simple"/></td>
		</tr>
		<tr>
		<td width="70" height="30">备注</td>
		<td colspan="3"><s:textfield name="input.remark" theme="simple"/></td>
		</tr>
		<tr>
		<td width="70" height="30">图片</td>
		<td colspan="3"><input type="file" name="input.image" /></td>
		</tr>
		<tr>
		<td colspan="4" align="center" height="30"><img src="../images/submit.png"/></td>
		</tr>
		
		</table>
	</div>

  </body>
</html>
