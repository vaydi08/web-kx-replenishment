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
    
    <link rel="stylesheet" type="text/css" href="images/style_easyui.css"/>
	
	<!-- EASYUI -->
	<link rel="stylesheet" type="text/css" href="script/easyui/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="script/easyui/themes/icon.css" />

	<script type="text/javascript" src="script/easyui/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="script/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="script/easyui/locale/easyui-lang-zh_CN.js"></script>
	
	<style>
	*{ margin:0px; padding:0px}
	body {
		font-family: "微软雅黑";
		font-size:12px;
		width:1024px;
		background:#e5e5e5
	}
	#left a {color:#F6AB00;font-weight:bold;letter-spacing:5px;width:100%;}

	#top {background:url(images/title_bar.jpg) no-repeat}
	#left {height:100%; padding:10px; background:#555; text-align:center;overflow-x:hidden;}
	#left li { list-style-type:none}
	
	#mainFrame {width:843px !important; width:820px;margin:0px;padding:0px;height:700px;}
	
	
   .icon-d1{
		background:url('images/button/d1.png') no-repeat;
	}
	.icon-d2{
		background:url('images/button/d2.png') no-repeat;
	}
	</style>
	
	<script>
	$(document).ready(function() {	
		$("#menu").accordion({
			height:$("#left").height()
		});

	});
	</script>
  </head>
  
  <body>
    <s:bean name="java.util.Date" var="now" />
    
    <table border="0" cellpadding="0" cellspacing="0">
    <!-- TOP -->
    <tr><td id='top' width="1024" height="71" colspan="2">
    <div class='topStr'>当前用户：<s:property value="#session[@com.sol.kx.web.common.Constants@SESSION_USER].username" /> <s:date name="#now" format="yyyy / MM / dd"/> 
    <a href='sys!login.action?input.username=sol&input.password=940430'>重新登录</a> 
    <a href="sys!main.action" target="mainFrame">首页</a></div>
    </td></tr>
    
    <!-- MAIN -->
    <tr>
    <!-- LEFT -->
    <td id="left" width="160" height=700">
	   <div id="menu">
		<div title="基础数据" iconCls="icon-d1" selected="true" style="overflow:hidden;padding:10px;">  
	        <a href="info/ProductManager.html" target="mainFrame" class="easyui-linkbutton" plain="true">产品数据</a>
	        <a href="info/ShopManager.html" target="mainFrame" class="easyui-linkbutton" plain="true">门店数据</a>  
	        <a href="info/CategoryManager.html" target="mainFrame" class="easyui-linkbutton" plain="true">分类列表</a>  
	        <a href="info/QuickLocator.html" target="mainFrame" class="easyui-linkbutton" plain="true">快速检索</a>  
	    </div>  
	    <div title="核定数据" iconCls="icon-d2" style="overflow:hidden;padding:10px;">  
	        <a href="stock/CheckChoose.html" target="mainFrame" class="easyui-linkbutton" plain="true">核定数据</a>
	        <a href="stock/CheckedManager.html" target="mainFrame" class="easyui-linkbutton" plain="true">已作核定</a>  
	    </div>  
	    <div title="智能比对" iconCls="icon-d2" style="overflow:hidden;padding:10px;">  
	        <a href="compare/CompareUpload.html" target="mainFrame" class="easyui-linkbutton" plain="true">比对数据</a>
	        <a href="compare/CargoUpload.html" target="mainFrame" class="easyui-linkbutton" plain="true">智能分货</a>
	    </div>
	    <div title="订购系统" iconCls="icon-d2" style="overflow:hidden;padding:10px;">  
	        <a href="order/self.html" target="mainFrame" class="easyui-linkbutton" plain="true">我的订单</a>
	        <a href="order/all.html" target="mainFrame" class="easyui-linkbutton" plain="true">全部订单</a>
	        <a href="sys!index.action" target="_top" class="easyui-linkbutton" plain="true">未处理订单</a>
	        <a href="order/orderSupplier.html" target="mainFrame" class="easyui-linkbutton" plain="true">供应商列表</a>
	    </div>
	    <div title="系统管理" iconCls="icon-d2" style="overflow:hidden;padding:10px;">  
	        <a href="UserManager.html" target="mainFrame" class="easyui-linkbutton" plain="true">用户管理</a>
	    </div>
	   </div>
    </td>
    <!-- FRAME -->
    <td style="background-color:#f6ab46">
    <iframe src="sys!main.action" frameborder="0" marginheight="0" marginwidth="0" scrolling="no" name="mainFrame" id="mainFrame"></iframe>
    </td>
    </tr>
    </table>

  </body>
</html>
