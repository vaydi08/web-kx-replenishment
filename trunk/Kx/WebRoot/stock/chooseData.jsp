<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
[
<s:iterator value="choose" status="idx">{"text":"<s:property value="key"/>","value":"<s:property value="value"/>"}<s:if test="not #idx.last">,</s:if></s:iterator>
]

