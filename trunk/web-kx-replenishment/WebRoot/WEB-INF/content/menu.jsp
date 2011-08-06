<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>menu.html</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    
	<link href="images/style.css" rel="stylesheet" type="text/css" />
	<script src="script/jquery/jquery.js"></script>
	
	<script src="script/leftmenu.js"></script>
	<script src="script/default.js"></script>
  </head>
  
  <body>
    <div id="left">
	  <ul>
	    <li><a href="#" class="menu"><img src="images/leftbutton/left1.png"/></a>
	      <ul class="submenu" id="memu1">
	        <li><a target="mainFrame" href='<s:url action="product" namespace="info" method="manager"/>'><img src="images/leftbutton/ls1_1.png"/></a></li>
	        <li><a target="mainFrame" href='<s:url action="product" namespace="info" method="manager"/>'><img src="images/leftbutton/ls1_2.png"/></a></li>
	        <li><a href=""><img src="images/leftbutton/ls1_3.png"/></a></li>
	        <li><a target="mainFrame" href='<s:url action="shop" namespace="info" method="manager"/>'><img src="images/leftbutton/ls1_4.png"/></a></li>
	        <li><a target="mainFrame" href='<s:url action="shop" namespace="info" method="manager"/>'><img src="images/leftbutton/ls1_5.png"/></a></li>
	        <li><a target="mainFrame" href='<s:url action="category" namespace="info" method="manager"/>'><img src="images/leftbutton/ls1_6.png"/></a></li>
	      </ul>
	    </li>
	    <li><a target="mainFrame" href='<s:url action="check" namespace="stock" method="preManager"/>'><img src="images/leftbutton/heding.png"/></a>
	    </li>
	    <li><a target="mainFrame" href='<s:url action="check" namespace="stock" method="preManager"/>'><img src="images/leftbutton/daoru.png"/></a>
	    </li>
	    <li><a href="#" class="menu"><img src="images/leftbutton/fenxi.png"/></a>
	      <ul class="submenu" id="memu1">
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls4_1.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls1.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls3.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls4.png"/></a></li>
	      </ul>
	    </li>
	    <li><a href="#" class="menu"><img src="images/leftbutton/fenhuo.png"/></a>
	      <ul class="submenu" id="memu1">
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls1.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls1.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls3.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls4.png"/></a></li>
	      </ul>
	    </li>
	    <li><a href="#" class="menu"><img src="images/leftbutton/dinggou.png"/></a>
	      <ul class="submenu" id="memu1">
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls1.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls1.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls3.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls4.png"/></a></li>
	      </ul>
	    </li>
	    <li><a href="#" class="menu"><img src="images/leftbutton/system.png"/></a>
	      <ul class="submenu" id="memu1">
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls1.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls1.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls3.png"/></a></li>
	        <li><a target="mainFrame"  href=""><img src="images/leftbutton/ls4.png"/></a></li>
	      </ul>
	    </li>

	  </ul>
	</div>
  </body>
</html>
