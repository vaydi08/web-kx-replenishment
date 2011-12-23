<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
[
<s:iterator value="quickLocatorStockList" status="st">
{
"treeid":"<s:property value="#st.index"/>",
"shopname":"<s:property value="key" escape="false"/>",
"minweight":"",
"maxweight":"",
"stock":"",
"stocktype":"",
"children":[
	<s:iterator value="value" status="typeSt">
	{
	"treeid":"<s:property value="#st.index"/>_<s:property value="id"/>",
	"shopname":"<s:property value="shopname" escape="false"/>",
	"minweight":"<s:property value="minweight"/>",
	"maxweight":"<s:property value="maxweight"/>",
	"stock_type1":"<s:property value="stock_type1"/>",
	"stock_type2":"<s:property value="stock_type2"/>"
	}<s:if test="not #typeSt.last">,</s:if>
	</s:iterator>
]}<s:if test="not #st.last">,</s:if>
</s:iterator>
]