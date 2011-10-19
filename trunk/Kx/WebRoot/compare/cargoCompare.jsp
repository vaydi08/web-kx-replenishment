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
	
	<!-- EXTENSION -->
	<script type=text/javascript src="../script/easyui/datagrid-detailview.js"></script>

	<!-- DEFAULT -->
	<script type="text/javascript" src="../script/sol-easyui-action.js"></script>

	<script>
	
	</script>
</head>
<body style="padding:10px;">
	<div class="easyui-tabs" style="height:600px">
	<div title="分货单" style="padding:10px;">

		<table class="easyui-datagrid" fitColumns="true" nowrap="true" singleSelect="true">
		<thead>
		<tr>
		<th field="shopname" width="80">门店名称</th>
		<th field="pname" width="80">产品名称</th>
		<th field="pcode" width="80">产品代码</th>
		<th field="minweight" width="80">克重范围</th>
		<th field="stock" width="80">核定库存</th>
		<th field="stocknow" width="80">实际库存</th>
		<th field="saletime" width="80">最后销售时间</th>
		<th field="serial" width="80">配货流水号</th>
		<th field="num" width="80">配货数量</th>
		<th field="minallot" width="80">至少还需配货</th>
		</tr>
		</thead>
		<s:iterator value="cargoBean.dataList">
		<tr>
		<td><s:property value="shopname" /></td>
		<td><s:property value="pname" /></td>
		<td><s:property value="pcode" /></td>
		<td><s:property value="minweight" /> - <s:property value="maxweight" /></td>
		<td><s:property value="stock" /></td>
		<td><s:property value="stocknow" /></td>
		<td><s:property value="saletime" /></td>
		<td><s:property value="serial" /></td>
		<td><s:property value="num" /></td>
		<td><s:property value="minallot" /></td>
		</tr>
		</s:iterator>
		</table>

	</div>
	
	<div title="货品分配" style="padding:10px">
		<table class="easyui-datagrid" fitColumns="true" nowrap="true" singleSelect="true">
		<thead>
		<tr>
		<th field="pname" width="80">产品名称</th>
		<th field="pcode" width="80">产品代码</th>
		<th field="serial" width="80">流水号</th>
		<th field="weight" width="80">克重</th>
		<th field="shopname" width="80">分配门店</th>
		</tr>
		</thead>
		<s:iterator value="cargoBean.stockList">
		<tr>
		<td><s:property value="pname" /></td>
		<td><s:property value="pcode" /></td>
		<td><s:property value="serial" />
		<td><s:property value="weight" /></td>
		<td><s:property value="shopname" /></td>
		</tr>
		</s:iterator>
		</table>
	</div>
	
	<s:if test="cargoBean.hasException()">
	<div title="处理异常" style="padding:10px" iconCls="icon-cancel">
	<s:property value="cargoBean.exception"/>
	</div>
	</s:if>
	
	<s:if test="cargoBean.hasReserve()">
	<div title="异常信息" style="padding:10px" iconCls="icon-cancel">
	<s:iterator value="cargoBean.reserve[0]">
	<p><s:property/></p>
	</s:iterator>
	</div>
	</s:if>
	
	<div title="下载数据" style="padding:10px" iconCls="icon-save">
	
	</div>
	</div>
</body>
</html>