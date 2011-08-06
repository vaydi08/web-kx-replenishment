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
	<script src="../script/default.js"></script>
		
	<!-- 分页插件 -->
	<!--默认主题-->
	<link href="../script/jpage/theme/default/css/jpage.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../script/jpage/jquery.cookie.js"></script>
	<script language="javascript" src="../script/jpage/jquery.jpage.js"></script>
	
	
	
	<script>
	$(document).ready(function() {
		var datasize = <s:property value="list.size()"/>;
		var dataBeforeStr = 
			'<table cellpadding="1" cellspacing="1" width="1024">' +
			'<thead><tr>' +
			'<th width="30"><input type="checkbox" id="allsel" /></th>' +
			'<th width="300">名称</th><th>代码</th>' +
			'</tr></thead>';
		// 分页插件
		var initData = [
			<s:iterator value="list" var="obj">
			'<tr><td align="center" style="padding-left:0px"><input type="checkbox" name="ids" class="tdcb" /></td>' + 
			'<td><s:property value="name" escape="false"/></td>' +
			'<td><s:property value="ccode" escape="false"/></td></tr>',
			</s:iterator>
			''
       	];
       	
       	$('#pagetb').jpage({totalRecord:datasize,dataStore:initData,groupSize:0,
			dataBefore:dataBeforeStr,
			dataAfter:'</table>'
        });
	});
	
	</script>
	
	
	
  </head>
  
  <body>
	
	<div id="right">
		<div class="query">
		<form action="category!manager.action" method="post">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
				<td width="110"><s:select list="#{1:'大类',2:'小类',3:'货型1',4:'货型2'}" name="input.clevel" theme="simple"/></td>
				<td ><input type="submit" value="查 询"/></td>
				</tr>
			</table>
		</form>
		</div>
		
		<div class="clear5"></div>
		
		<div class="list">
			<table cellpadding="1" cellspacing="1" width="1024">
				<thead>
				<tr><td align="right"><a href="category!preNew.action">新增分类</a> <a href="#">删除</a>&nbsp;</td></tr>
				</thead>
			</table>
		   <div id="pagetb"></div>

		</div>
	</div>
	
  </body>
</html>
