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
	<div title="补货建议单" style="padding:10px;">
	
	<table class="easyui-datagrid" fitColumns="true" nowrap="true" singleSelect="true">
	<thead>
	<tr>
	<th field="pname" width="80">产品名称</th>
	<th field="pcode" width="80">产品代码</th>
	<th field="minweight" width="80">克重范围</th>
	<th field="stock" width="80">核定库存</th>
	<th field="kucun" width="80">实际库存</th>
	<th field="need" width="80">建议补货量</th>
	</tr>
	</thead>
	<s:iterator value="supplyList">
	<tr>
	<td><s:property value="pname" /></td>
	<td><s:property value="pcode" /></td>
	<td><s:property value="minweight" /> - <s:property value="maxweight" /></td>
	<td><s:property value="stock" /></td>
	<td><s:property value="kucun" /></td>
	<td><s:property value="need" /></td>
	</tr>
	</s:iterator>
	</table>
	</div>
	
	<div title="分货建议单" style="padding:10px">
	
	</div>
	
	<div title="下载数据" style="padding:10px" iconCls="icon-save">
	
	</div>
	</div>
</body>
</html>