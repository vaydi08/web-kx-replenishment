<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
[
<s:iterator value="comboboxBean.dataList" status="st">
{"text":"<s:property value="text" escape="false"/>","value":"<s:property value="value" escape="false"/>"<s:if test="selected">,"selected":true</s:if>}<s:if test="not #st.last">,</s:if>
</s:iterator>
] 