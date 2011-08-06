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

       	// 全选
       	$("#allsel").click(function() {
			if($(this).attr("checked")) {
				$(".tdcb").each(function() {$(this).attr("name");$(this).attr("checked",true);});
			} else {
				$(".tdcb").each(function() {$(this).removeAttr("checked");});
			}				
		});

	});

	</script>

	<style type="text/css">
	.checkInput {width:50px}
	</style>	
	
	
  </head>
  
  <body>
	
	<div id="right">
		<div class="query">
		<form action="check!manager.action" method="post">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
				<td width="50" height="30">门店</td>
				<td width="110"><s:select list="shopList" listKey="id" listValue="name" name="input.shopid" theme="simple"/></td>
				<td width="110"><s:select list="#{1:'日常',2:'节假日'}" name="input.stocktype" value="stocktype" theme="simple"/></td>
				<td ><input type="submit" value="查 询"/></td>
				</tr>
			</table>

		</form>
		</div>
		
		<div class="clear5"></div>
		
		<div class="list">
			<s:if test="list != null">
			<table cellpadding="1" cellspacing="1" width="1024">
				<thead>
				<tr><td align="right"><a href="check!save.action">保存核定数据</a>&nbsp;<a href="#">删除</a>&nbsp;</td></tr>
				</thead>
			</table>

			<table cellpadding="1" cellspacing="1" width="1024" id="paget">
				<thead>
				<tr>
					<th width="30"><input type="checkbox" id="allsel" /> </th>
					<th>门店</th>
				    <th>商品</th>
				    <th>克重范围</th>
				    <th>核定库存</th>
			    </tr>
			    </thead>
			    
			    <s:iterator value="list">
			    <tr>
			    	<td><s:checkbox name="ids[]" cssClass="tdcb" theme="simple"/></td>
					<td><s:property value="shopid"/></td>
				    <td><s:property value="pid"/></td>
				    <td><s:textfield name="minweight" cssClass="checkInput" theme="simple"/> - 
				   		<s:textfield name="maxweight" cssClass="checkInput" theme="simple"/></td>
				    <td><s:textfield name="stock" cssClass="checkInput" theme="simple"/></td>
			    </tr>
			    </s:iterator>

			  </table>

			</s:if>

		</div>
	</div>
	
  </body>
</html>
