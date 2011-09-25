<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- NORMAL STYLE 
	<link rel="stylesheet" type="text/css" href="../images/style_easyui.css">
-->
  </head>
  
  <body>
	<table cellpadding="0" cellspacing="0" border="0" class="easyui-datagrid" fitColumns="true" nowrap="true" title="产品列表">
	<thead>
	<tr>
	<th field="quality" width="80">成色</th>
	<th field="pweight" width="80">金重</th>
	<th field="stand" width="80">规格</th>
	<th field="premark" width="100">备注</th>
	<th field="image" width="150">图片</th>
	</tr>
	</thead>
	<tbody>
    <s:iterator value="detailList">
    <tr>
    <td><s:property value="quality" /></td>
    <td><s:property value="pweight" /></td>
    <td><s:property value="stand" /></td>
    <td><s:property value="premark" /></td>
    <td><img src="../images/product/<s:property value="image" />" height="80"/></td>
    </tr>
    </s:iterator>
    </tbody>
	</table>
  </body>
</html>
