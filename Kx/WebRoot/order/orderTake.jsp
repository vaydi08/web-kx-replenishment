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
		$("#supplier").combobox({
			width:151,
			url:"supplier!suppliers.action",
			valueField:'text',
			onSelect:function(record) {
				$("#supplier_contact").val(record.reserve);
			}
		});

		$("#form").form({
			url:'feedback!add2.action',
			onSubmit:function() {return $("#form").form('validate');},
			success:function(data){
				var result = eval('(' + data + ')');
				if(result.success)
					location.href = 'order!order3Repost.action?input.id=<s:property value="order.id"/>';
				else
					$.messager.show({title:"Error",msg:result.msg});
			}
		});
	});

	var cancelOrder = function() {
		$.messager.prompt('取消确认', '确定要取消此订单,订单取消后就不能再进行处理<br/>请输入订单取消的原因:', function(r){
			if(r!= null && r != '') {
				$.post('order!orderCancel.action',{'input.id':<s:property value="order.id"/>,'input.cancelReason':r},function(data){
					var result = eval('(' + data + ')');
					if(result.success)
						location.href = 'self.html';
					else
						$.messager.show({title:"Error",msg:result.msg});
				});
			}
		});
	}
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
			<td></td><td></td>
			</tr>
			</tbody>
			</table>
			</div>
			
			<div class="clear"></div>

			<div class="easyui-panel" style="height:170px;padding:10px;background-color:#fafafa;" title="等待确认" iconCls="icon-ok">
			<form id="form" method="post">
			<input type="hidden" name="input.orderid" value="<s:property value="order.id"/>" />
			<table cellpadding="0" cellspacing="0" width="700">
			<tbody>
			<tr>
			<td width="100" height="30">接单人</td><td width="170"><s:property value="order.username"/></td>
			<td width="100">接单时间</td><td width="170"><s:date name="order.gettime" format="yyyy-MM-dd HH:mm:ss"/></td>
			<td><a href="javascript:cancelOrder()" id="cancelOrder" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">取消此订单</a></td>
			</tr>
			<tr>
			<td height="30">联系供应商</td>
			<td><select id="supplier" name="input.supplier"></select>
			</td>
			<td>联系人</td><td><input type="text" id="supplier_contact" name="input.contact"/></td>
			<td></td>
			</tr>
			<tr>
			<td height="30">订购重量(数量)</td><td colspan="3"><input class="easyui-numberbox" required="true" min="1" precision="0" type="text" name="input.ordernum" /></td>
			</tr>
			<tr>
			<td height="30" colspan="4" align="center"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="$('#form').submit()">提交</a></td><td></td>
			</tr>
			</tbody>
			</table>
			</form>
			</div>
			
			<div class="clear"></div>
			
			<div class="easyui-panel" style="height:70px;padding:10px;background-color:#fafafa;" title="业务处理" iconCls="icon-redo">
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