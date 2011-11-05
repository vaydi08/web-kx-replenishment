<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>中街大悦城 活动</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	
	<script src="script/sol/scroll.js"></script>
	<script src="script/jquery/jquery.js"></script>
		
	

	<style type="text/css">
	body{margin:0px;padding:0px;}
	#left {position:absolute; width:50px; height:51px; z-index:7; left: 0px; top: 90px;}
	#right {position:absolute; width:50px; height:51px; z-index:8; left: 825px; top: 90px}
	
	#divCont {position:absolute; z-index:4; overflow: hidden; background-color:#fff;left:20px}
	#divText{position:absolute; z-index:3; left: 0px; top: 0px}

	
	a {text-decoration:none; color:#000}
	a img {border:0px}
	</style>
	
	<script>

	$(document).ready(function(){
		// 设定长宽
		$("#divCont").width(800);
		$("#divCont").height(190);

	});
	</script>
  </head>
  
	<body onload="scrollInit()">
	<div id="divCont">
		<div class="yoxview" id="divText">
		
		<table cellpadding="0" cellspacing="0" width="<s:property value="90 * splitLen"/>" border="0" id="listTable">
		<tr>
		<s:iterator value="imgList" status="idx">
		<td width="90" align="center"><a id="img<s:property value="id"/>" href="pic/<s:property value="image"/>">
		<img width="82" height="60" src="pic/<s:property value="image"/>" alt="<s:property value="text" escape="false"/>" title="<s:property value="text" escape="false"/>" /></a>
		</td>
		<s:if test="(#idx.index+1) % splitLen == 0"></tr><tr></s:if>
		</s:iterator>
		</tr>
		</table>
		</div>
	</div>
	
	<div>
	<div id="left"><a href="javascript:void(0)" onmousedown="scrollV(-10)" onmouseout="noScroll()" onmouseover="scrollV(-5)"><img src="images/left.gif" width="20"/></a></div>
	<div id="right"><a href="javascript:void(0)" onmousedown="scrollV(10)" onmouseout="noScroll()" onmouseover="scrollV(5)"><img src="images/right.gif" width="20"/></a></div>
	</div>
	
	</body>
</html>
