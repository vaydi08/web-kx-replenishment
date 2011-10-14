<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<!-- NORMAL STYLE -->
	<link rel="stylesheet" type="text/css" href="images/style_easyui.css"/>
	
	<!-- EASYUI -->
	<link rel="stylesheet" type="text/css" href="script/easyui/themes/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="script/easyui/themes/icon.css"/>

	<script type="text/javascript" src="script/easyui/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="script/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="script/easyui/locale/easyui-lang-zh_CN.js"></script>
	
	<!-- DEFAULT -->
	<script type="text/javascript" src="script/sol-easyui-action.js"></script>
	
	<script>
	$(document).ready(function() {
		// 表格
		$('#listTable').grid({
			title:"有新的订单",
			height:330,
			url:'order/order!managerUntake.action',
			toolbar:[{
				id:'btngetorder',
				text:'接单',
				iconCls:'icon-print',
				handler:function(){
					var row = $("#listTable").datagrid('getSelected');
					if(row) {
						$.messager.confirm('订单', '接受此订单，将进入处理流程', function(r){
							if (r){
								location.href = "order/order!orderTake.action?input.id=" + row.id
								+ "&input.userid=" + <s:property value="#session[@com.sol.kx.web.common.Constants@SESSION_USER].id" />;
							}
						});
					}
				}
				},{
					id:'btncancelorder',
					text:'取消订单',
					iconCls:'icon-remove',
					handler:function(){
						var row = $("#listTable").datagrid('getSelected');
						if(row) {
							$.messager.prompt('取消确认', '确定要取消此订单,订单取消后就不能再进行处理<br/>请输入订单取消的原因:', function(r){
								if(r!= null && r != '') {
									$.post('order/order!orderCancel.action',{'input.id':row.id,'input.cancelReason':r},function(data){
										var result = eval('(' + data + ')');
										if(result.success)
											$("#listTable").datagrid('reload');
										else
											$.messager.show({title:"Error",msg:result.msg});
									});
								}
							});
						}
					}
				},'-',{
					id:'myorder',
					text:'我的订单',
					iconCls:'icon-ok',
					handler:function(){
						location.href='order/self.html';
					}
				},'-',{
					id:'allorder',
					text:'全部订单',
					iconCls:'icon-ok',
					handler:function(){
						location.href='order/all.html';
					}
				}]
		});
		
	});
	
	</script>

	
  </head>
  
  <body>
	
	<div id="right">
		
		<div class="list" >

			<table id="listTable" iconCls="icon-tip" nowrap="true" singleSelect="true"
				 pagination="true" rownumbers="true">
			<thead>
			<tr>
			<th field="id" width="100">订单编号</th>
			<th field="pname" width="100">产品名称</th>
			<th field="pcode" width="100">产品代码</th>
			<th field="shopname" width="100">订购门店</th>
			<th field="fromwho" width="100">下单人员</th>
			<th field="ordertime" width="100">下单时间</th>
			</tr>
			</thead>
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
