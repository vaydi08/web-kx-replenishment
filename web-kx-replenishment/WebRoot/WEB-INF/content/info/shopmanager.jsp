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
			<s:iterator value="list" var="obj">'<tr><td align="center" style="padding-left:0px"><input type="checkbox" name="ids" class="idcb" /></td><td><s:property value="name" escape="false"/></td><td><s:property value="scode" escape="false"/></td><td><s:property value="address" escape="false"/></td><td><s:property value="tel" escape="false"/></td><td><s:property value="remark" escape="false"/></td></tr>',
			</s:iterator>
			''
       	];
       	
       	$('#pagetb').jpage({totalRecord:<s:property value="list.size()"/>,dataStore:initData,groupSize:0,
			dataBefore:'<table cellpadding="1" cellspacing="1" width="100%"><thead><tr><th width="30"><input type="checkbox" id="allsel" /> </th><th>门店</th><th>编号</th><th>地址</th> <th>电话</th><th>备注</th></tr></thead>',
			dataAfter:'</table>'
        });

       	// 全选
       	$("#allsel").click(function() {
			if($(this).attr("checked")) {
				$(".idcb").each(function() {$(this).attr("name");$(this).attr("checked",true);});
			} else {
				$(".idcb").each(function() {$(this).removeAttr("checked");});
			}				
		});

	});
	
	</script>
	
	
	
  </head>
  
  <body>
	
	<div id="right">

		<div class="list">
			<table cellpadding="1" cellspacing="1" width="100%">
				<thead>
				<tr><td align="right"><a href="shop!preNew.action">新增门店</a> <a href="#">删除</a>&nbsp;</td></tr>
				</thead>
			</table>

			<div id="pagetb"></div>

		</div>
	</div>
	
  </body>
</html>
