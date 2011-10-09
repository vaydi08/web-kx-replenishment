<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<!-- NORMAL STYLE -->
	<link rel="stylesheet" type="text/css" href="images/style_easyui.css">
	
	<!-- EASYUI -->
	<link rel="stylesheet" type="text/css" href="script/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="script/easyui/themes/icon.css">

	<script type="text/javascript" src="script/easyui/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="script/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="script/easyui/locale/easyui-lang-zh_CN.js"></script>
	
	<!-- DEFAULT -->
	<script type="text/javascript" src="script/sol-easyui-action.js"></script>
	
	<script>
	$(document).ready(function() {
		// 表格
		$('#listTable').grid({
			title:'有新的订单',
			height:330,
			url:'order/order!managerUntake.action',
			multi:false,
			columns:[[   
		        {field:'id',title:'订单编号',width:140},
		        {field:'pname',title:'产品名称',width:140},
		        {field:'pcode',title:'产品代码',width:140},
		        {field:'shopname',title:'订购门店',width:140},
		        {field:'fromwho',title:'下单人员',width:140},
		        {field:'ordertime',title:'下单时间',width:140}
		    ]],
			toolbar:[{
				id:'btngetorder',
				text:'接单',
				iconCls:'icon-print',
				handler:function(){
					var row = $("#listTable").datagrid('getSelected');
					if(row) {
						$.messager.confirm('订单', '接受此订单，将进入处理流程', function(r){
							if (r){
								location.href = "order/order!orderTake.action?input.id=" + row.id;
							}
						});
					}
				}
				},{
					id:'btncancelorder',
					text:'取消订单',
					iconCls:'icon-remove',
					handler:function(){
						var pcodes = [];
						var rows = $('#listTable').datagrid('getSelections');
						for(var i=0;i<rows.length;i++){
							pcodes.push(rows[i].pcode);
						}
						alert(pcodes.join(':'));
					}
				},{
					id:'myorder',
					text:'我正在处理的订单',
					handler:function(){
						$('#listTable')
					}
				}]
		});
		
	});
	
	</script>

	
  </head>
  
  <body>
	
	<div id="right" style="background-image:url(images/background2.png)">
		
		<div class="list" style="background-color:#fafafa">

			<table id="listTable" title="有新的订单" iconCls="icon-tip" nowrap="true" singleSelect="false"
				 pagination="true" rownumbers="true" >

			</table>

		</div>
		
		<div>
		<table border="0" cellspacing="0" cellpadding="0" align="center" width="540" style="position:relative;bottom:-200px">
		<tr>
		<td><a href="compare/compare.html"><img src="images/1_03.png" /></a></td>
		<td><a href="compare/compare.html"><img src="images/1_05.png" /></a></td>
		<td><a href="order/purchase.html"><img src="images/1_06.png" /></a></td>
		</tr>
		</table>
		</div>
	</div>
	
  </body>
</html>