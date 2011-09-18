<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
{
	"success":<s:property value="success"/>,
	<s:if test="not success">
	"msg":"<s:property value="errorMsg" escape="false"/>"
	</s:if>
	<s:else>
	"files":[
	<s:iterator value="result" status="idx">
	{"count":<s:property value="count"/>,"success":<s:property value="success"/>,"msg":[
	<s:iterator value="errorMsg" status="erridx">"<s:property escape="false"/>"<s:if test="not #erridx.last">,</s:if></s:iterator>]}<s:if test="not #idx.last">,</s:if>
	</s:iterator>
	]
	</s:else>
}