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
		
	<!-- 分页插件 -->
	<!--默认主题-->
	<link href="../script/jpage/theme/default/css/jpage.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../script/jpage/jquery.cookie.js"></script>
	<script language="javascript" src="../script/jpage/jquery.jpage.js"></script>
	
	
	
	<script>
	$(document).ready(function() {
		// 分页插件
		var initData = [
			<s:iterator value="productList" var="obj">'<tr><td align="center" style="padding-left:0px"><input type="checkbox" name="ids" class="imgcb" /></td><td class="imgtd"><img src=<s:property value="sysEnvironment.imageFolder + image"/> /></td><td><s:property value="type1.name" escape="false"/></td><td><s:property value="type2.name" escape="false"/></td><td><s:property value="type3.name" escape="false"/></td><td><s:property value="type4.name" escape="false"/></td><td><s:property value="name" escape="false"/></td><td><s:property value="pcode" escape="false"/></td><td><s:property value="quality" escape="false"/></td><td><s:property value="weight" escape="false"/></td><td><s:property value="stand" escape="false"/></td><td><s:property value="unit" escape="false"/></td><td><s:property value="remark" escape="false"/></td></tr>',
			</s:iterator>
			''
       	];
       	
       	$('#pagetb').jpage({totalRecord:<s:property value="productList.size()"/>,dataStore:initData,groupSize:0,
			dataBefore:'<table cellpadding="1" cellspacing="1" width="1024"><thead><tr><th width="30"><input type="checkbox" id="allsel" /> </th><th class="imgtd">&nbsp;</th><th>大类</th><th>小类</th><th>货型1</th> <th>货型2</th><th>商品品名</th> <th>商品代码</th><th>金属成色</th><th>金重</th><th>规格</th><th>单位</th><th>备注</th></tr></thead>',
			dataAfter:'</table>'
        });

       	// 全选
       	$("#allsel").click(function() {
			if($(this).attr("checked")) {
				$(".imgcb").each(function() {$(this).attr("name");$(this).attr("checked",true);});
			} else {
				$(".imgcb").each(function() {$(this).removeAttr("checked");});
			}				
		});

		$("#showImage").click(function() {
			toggleTd($("#showImage"),$(".imgtd"));
		});
		function toggleTd(ctrl,td) {
			if(ctrl.attr("checked")){
				td.each(function() {$(this).show()});
			} else {
				td.each(function() {$(this).hide()});
			}
		}
		toggleTd($("#showImage"),$(".imgtd"));
	});
	
	</script>
	
	
	
  </head>
  
  <body>
	
	<div id="right">
		<div class="query">
		<form action="product!manager.action" method="post">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
				<td width="35" height="30">大类</td>
				<td width="110"><s:select list="categoryLevel1" listKey="id" listValue="name" name="category1" theme="simple"/></td>
				<td width="35">小类</td>
				<td width="110"><s:select list="categoryLevel2" listKey="id" listValue="name" name="category2" theme="simple"/></td>
				<td width="40">货型1</td>
				<td width="110"><s:select list="categoryLevel3" listKey="id" listValue="name" name="category3" theme="simple"/></td>
				<td width="40">货型2</td>
				<td width="110"><s:select list="categoryLevel4" listKey="id" listValue="name" name="category4" theme="simple"/></td>
				</tr>
			</table>
			<table cellpadding="0" cellspacing="0" border="0" width="600">
				<tr>
				<td width="110"><s:select list="#{'key':'商品品名','pcode':'商品代码','quality':'金属成色','weight':'金重','stand':'规格','unit':'单位','remark':'备注'}" name="queryName" theme="simple"/></td>
				<td width="170"><s:textfield name="queryValue" theme="simple"/> </td>
				<td ><input type="submit" value="查 询"/></td>
				<td align="right"><s:checkbox name="showImage" id="showImage" theme="simple"/>显示图片</td>
				</tr>
			</table>
		</form>
		</div>
		
		<div class="clear5"></div>
		
		<div class="list">
			<table cellpadding="1" cellspacing="1" width="1024">
				<thead>
				<tr><td align="right"><a href="product!preNew.action">新增商品</a> <a href="#">删除</a>&nbsp;</td></tr>
				</thead>
			</table>
			<!-- 
			<table cellpadding="1" cellspacing="1" width="1024" id="paget">
				<thead>
				<tr>
					<th><input type="checkbox" id="allsel" /> </th>
					<th>&nbsp;</th>
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

			  </table>
			   -->
			   <div id="pagetb"></div>

		</div>
	</div>
	
  </body>
</html>
