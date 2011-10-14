<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<!-- NORMAL STYLE -->
	<link rel="stylesheet" type="text/css" href="../images/style_easyui.css" />
	
	<!-- EASYUI -->
	<link rel="stylesheet" type="text/css" href="../script/easyui/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="../script/easyui/themes/icon.css" />

	<script type="text/javascript" src="../script/easyui/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../script/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../script/easyui/locale/easyui-lang-zh_CN.js"></script>

	<!-- DEFAULT -->
	<script type="text/javascript" src="../script/sol-easyui-action.js"></script>

	<script>
	var statusMap = {'-1':{text:'已取消',color:'#708090'},
			'1':{text:'等待处理',color:'#00FFFF'},
			'2':{text:'已接单,等待确认',color:'#D2691E'},
			'3':{text:'供应商反馈',color:'#00008B'},
			'4':{text:'供应商发货',color:'#1E90FF'},
			'5':{text:'发送产品',color:'#D2B48C'},
			'6':{text:'订购完成',color:'#00FFFF'}};
	
	$(document).ready(function() {
		$("#listTable").datagrid({
			height:400,
			title:"供应商情况列表",
			style:{'background-color':'#fafafa'},
			url:'feedback!manager.action?input.orderid=<s:property value="order.id"/>',
			onLoadSuccess:function(data) {
				$("#feedbackid").val(data.reserve[0]);
			}
		});

		$("#status").text(statusMap[<s:property value="order.status"/>].text);
	});

	</script>
	
	</head>
	<body>
		<div id="right">

			<div class="easyui-panel" style="padding:10px;background-color:#fafafa;" title="订购信息" iconCls="icon-tip" >
			<table border="0" cellpadding="0" cellspacing="0" width="700">
			<tbody>
			<tr>
			<td width="100">订单号</td><td width="170"><s:property value="order.id"/></td>
			<td width="100">订购人</td><td width="170"><s:property value="order.fromwho"/></td>
			<td rowspan="3" align="center"><img height="60" src="../images/product/<s:property value="order.image"/>" /></td>
			</tr>
			<tr>
			<td>产品名称</td><td><s:property value="order.pname"/></td>
			<td width="100">产品代码</td><td><s:property value="order.pcode"/></td>
			</tr>
			<tr>
			<td>产品重量</td><td><s:property value="order.pweight"/></td>
			<td width="100">产品规格</td><td><s:property value="order.stand"/></td>
			</tr>
			<tr>
			<td>订购门店</td><td><s:property value="order.shopname"/></td>
			<td>下单时间</td><td><s:date name="order.ordertime" format="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
			<tr>
			<td>请求期限</td><td><s:date name="order.requesttime" format="yyyy-MM-dd HH:mm:ss"/></td>
			<td>&nbsp;</td><td>&nbsp;</td>
			</tr>
			</tbody>
			</table>
			</div>
			
			<div class="clear"></div>

			<table id="listTable" rownumbers="true" fitColumns="true" singleSelect="true">
			<thead>
			<tr>
			<th field="supplier" width="80">供应商</th>
			<th field="contact" width="80">联系人</th>
			<th field="ordernum" width="80">订购数量</th>
			<th field="settime" width="80">联系时间</th>
			<th field="feedback" width="80">反馈情况</th>
			<th field="feedbacktime" width="80">反馈时间</th>
			</tr>
			</thead>
			</table>
			
			<div class="clear"></div>
			
			<div class="easyui-panel" style="height:70px;padding:10px;background-color:#fafafa;" title="业务处理" iconCls="icon-redo">
			<table cellpadding="0" cellspacing="0" width="700">
			<tbody>
			<tr>
			<td>订单处理状态: <span id="status"></span></td>
			</tr>
			</tbody>
			</table>
			</div>
		
		</div>
	</body>
</html>
