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
	<script type="text/javascript" src="script/yoxview/yoxview-init.js"></script>
	
	

	<style type="text/css">
	body{background:url('images/bk.jpg') no-repeat;}
	
	#left {position:absolute; width:50px; height:51px; z-index:7; left: 40px; top: 580px;}
	#right {position:absolute; width:50px; height:51px; z-index:8; left: 1310px; top: 580px}
	
	
	#divCont {position:absolute; z-index:4; left: 90px; top: 420px; overflow: hidden; background-color:#fff;}
	#divText{position:absolute; z-index:3; left: 0px; top: 3px}
	#weibo {position:absolute;z-index:4;left:1213px;top:55px;}
	#search {position:absolute;z-index:4;left:1235px;top:86px;}
	* html #search{top:81px}
	*+html #search{top:81px}
	#write {position:absolute;z-index:4;left:1100px;top:81px;}
	#searchbtn {position:absolute;z-index:4;left:1310px;top:81px;}
	#reloadbtn {position:absolute;z-index:4;left:1340px;top:81px;}
	
	a {text-decoration:none; color:#000}
	a img {border:0px}
	</style>
	
	<script>

	$(document).ready(function(){
		// 设定长宽
		$("#divCont").width(1220);
		$("#divCont").height(350);

		$(".yoxview").yoxview({
			lang:'zh-cn',
			langFolder:'script/yoxview/lang/'
		});

		// 搜索按钮
		$("#btn_search").click(function(){
			var text = $("#text").val();

			$("img[alt*=" + text +"]").parent().click();
		});
	});
	</script>
  </head>
  
	<body onload="scrollInit()">
	<div id="divCont">
		<div class="yoxview" id="divText">
		
		<table cellpadding="0" cellspacing="0" width="<s:property value="160 * splitLen"/>" border="0" id="listTable">
		<tr>
		<s:iterator value="imgList" status="idx">
		<td width="160" align="center"><a id="img<s:property value="id"/>" href="pic/<s:property value="image"/>">
		<img width="150" height="110" src="pic/<s:property value="image"/>" alt="<s:property value="text" escape="false"/>" title="<s:property value="text" escape="false"/>" /></a>
		</td>
		<s:if test="(#idx.index+1) % splitLen == 0"></tr><tr></s:if>
		</s:iterator>
		</tr>
		</table>
		</div>
	</div>
	
	<div>
	<div id="left"><a href="javascript:void(0)" onmousedown="scrollV(-10)" onmouseout="noScroll()" onmouseover="scrollV(-5)"><img src="images/left.gif"/></a></div>
	<div id="right"><a href="javascript:void(0)" onmousedown="scrollV(10)" onmouseout="noScroll()" onmouseover="scrollV(5)"><img src="images/right.gif"/></a></div>
	</div>
	
	<div id="weibo">
	<iframe width="63" height="24" frameborder="0" allowtransparency="true" marginwidth="0" marginheight="0" scrolling="no" frameborder="No" border="0" src="http://widget.weibo.com/relationship/followbutton.php?width=63&height=24&uid=1844262925&style=1&btn=red&dpc=1"></iframe>
	</div>
	
	<div id="write">
	<a href="upload.html"><img src="images/blank_write.gif"/></a>
	</div>
	<div id="searchbtn">
	<a href="javascript:void(0)" id="btn_search"><img src="images/blank_search.gif"/></a>
	</div>
	<div id="reloadbtn">
	<a href="javascript:void(0)" onclick="location.reload()"><img src="images/blank_search.gif"/></a>
	</div>
	<div id="search">
	<input type="text" style="border:0px;width:70px;height:12px;background-color:#eaeaea;" id="text"/>
	</div>
	</body>
</html>
