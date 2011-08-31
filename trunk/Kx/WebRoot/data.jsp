<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
{
	"total":<s:property value="pagerBean.count"/>,
	"rows":[
	<s:iterator value="pagerBean.dataList" status="st">
	{
	<s:iterator value="json" status="mapst">
	"<s:property value="key" escape="false"/>":"<s:property value="value" escape="false"/>"<s:if test="not #mapst.last">,</s:if>
	</s:iterator>
	}<s:if test="not #st.last">,</s:if>
	</s:iterator>
	]
}