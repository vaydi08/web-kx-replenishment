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
		$("#allsel").click(function() {
			if($(this).attr("checked")) {
				$(".imgcb").each(function() {$(this).attr("name");$(this).attr("checked",true);});
			} else {
				$(".imgcb").each(function() {$(this).removeAttr("checked");});
			}				
		});
	});
	</script>
  </head>
  
  <body>
    <sol:top />
    
	<div id="mainFrame" class="clearfix">
	<!-- 左侧菜单 -->
	<%@include file="../menu.inc" %>
	
	
	<div id="right">
		<div class="query">
		<form action="product!manager.action" method="post">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
				<td>大类</td>
				<td><s:select list="categoryLevel1" listKey="id" listValue="name" name="category1" theme="simple"/></td>
				<td>小类</td>
				<td><s:select list="categoryLevel2" listKey="id" listValue="name" name="category2" theme="simple"/></td>
				<td>货型1</td>
				<td><s:select list="categoryLevel3" listKey="id" listValue="name" name="category3" theme="simple"/></td>
				<td>货型2</td>
				<td><s:select list="categoryLevel4" listKey="id" listValue="name" name="category4" theme="simple"/></td>
				</tr>
			</table>
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
				<td><s:select list="#{'key':'商品品名','pcode':'商品代码','quality':'金属成色','weight':'金重','stand':'规格','unit':'单位','remark':'备注'}" name="queryName" theme="simple"/></td>
				<td><s:textfield name="queryValue" theme="simple"/> </td>
				<td><s:checkbox name="showImage" theme="simple"/></td><td>显示图片</td>
				<td><input type="submit" value="查 询"/></td>
				</tr>
			</table>
		</form>
		</div>
		
		<div class="clear5"></div>
		
		<div class="list">
			<table cellpadding="1" cellspacing="1" width="1024">
				<thead>
				<tr><td align="right"><a href="#">新增商品</a> <a href="#">删除</a>&nbsp;</td></tr>
				</thead>
			</table>
			<table cellpadding="1" cellspacing="1" width="1024">
				<thead>
				<tr>
					<th><input type="checkbox" id="allsel" /> </th>
					<s:if test="isShowImage()"><th>&nbsp;</th></s:if>
				    <th>大类</th>
				    <th>小类</th>
				    <th>货型1</th>
				    <th>货型2</th>
				    <th>商品品名</th>
				    <th>商品代码</th>
				    <th>金属成色</th>
				    <th>金重</th>
				    <th>规格</th>
				    <th>单位</th>
				    <th>备注</th>
			    </tr>
			    </thead>
			    <s:iterator value="result.result" var="obj">
			    <tr>
			    	<td align="center" style="padding-left:0px"><s:checkbox name="ids" theme="simple" cssClass="imgcb" /> </td>
			    	<s:if test="isShowImage()"><td><img src='<s:property value="sysEnvironment.imageFolder + #obj[4].image"/>' /></td></s:if>
					<td><s:property value="#obj[0].name"/></td>
					<td><s:property value="#obj[1].name"/></td>
					<td><s:property value="#obj[2].name"/></td>
					<td><s:property value="#obj[3].name"/></td>
					<td><s:property value="#obj[4].name"/></td>
					<td><s:property value="#obj[4].pcode"/></td>
					<td><s:property value="#obj[4].quality"/></td>
					<td><s:property value="#obj[4].weight"/></td>
					<td><s:property value="#obj[4].stand"/></td>
					<td><s:property value="#obj[4].unit"/></td>
					<td><s:property value="#obj[4].remark"/></td>
			    </tr>
			    </s:iterator>
			  </table>
		</div>
	</div>
	
	</div>
  </body>
</html>
