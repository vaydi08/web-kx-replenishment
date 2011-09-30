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
	$(document).ready(function() {
		
	});

	</script>
	
	</head>
	<body>
		<div id="right">
		
			<div class="easyui-panel" style="padding:10px;" title="订购信息" iconCls="icon-tip" >
			<table border="0" cellpadding="0" cellspacing="0" width="700">
			<tbody>
			<tr>
			<td width="100">订单号</td><td width="170">00000001</td>
			<td width="100">订购人</td><td width="170">张三</td>
			<td rowspan="3" align="center"><img height="60" src="../images/product/201081215217.png" /></td>
			</tr>
			<tr>
			<td>产品名称</td><td>00000001</td>
			<td width="100">产品代码</td><td>A0001</td>
			</tr>
			<tr>
			<td>订购门店</td><td>上南店</td>
			<td>下单时间</td><td>2011-07-21 15:34:29</td>
			</tr>
			</tbody>
			</table>
			</div>
			
			<div class="clear"></div>

			<div class="easyui-panel" style="height:170px;padding:10px;" title="等待确认" iconCls="icon-ok">
			<table cellpadding="0" cellspacing="0" width="700">
			<tbody>
			<tr>
			<td width="100" height="30">接单人</td><td width="170">李四</td>
			<td width="100">接单时间</td><td width="170">2011-07-21 18:12:58</td>
			<td><a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">取消此订单</a></td>
			</tr>
			<tr>
			<td height="30">联系供应商</td>
			<td><select class="easyui-combobox" style="width:151px"><option value="1">老凤祥</option><option value="2">亚一金店</option></select>
			</td>
			<td>联系人</td><td><input type="text" value="JACK" /></td>
			<td></td>
			</tr>
			<tr>
			<td height="30">订购重量(数量)</td><td colspan="3"><input class="easyui-numberbox" required="true" min="1" precision="2" type="text" value="" /></td>
			</tr>
			<tr>
			<td height="30" colspan="4" align="center"><a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="location.href='orderStep2.html'">提交</a></td><td></td>
			</tr>
			</tbody>
			</table>
			</div>
			
			<div class="clear"></div>
			
			<div class="easyui-panel" style="height:70px;padding:10px;" title="业务处理" iconCls="icon-redo">
			<table cellpadding="0" cellspacing="0" width="700">
			<tbody>
			<tr>
			<td>订单处理: <span style="color:red">等待确认</span> -> 确认成功,供应商反馈 -> 供应商回复 -> 订购完成 -> 货品输送</td>
			</tr>
			</tbody>
			</table>
			</div>
		
		</div>
	</body>
</html>
