<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
{
	<s:if test="productPagerBean.reserve != null">
	"shopname":"<s:property value="productPagerBean.reserve[0]" escape="false"/>",
	"typename":"<s:property value="productPagerBean.reserve[1]" escape="false"/> <s:property value="productPagerBean.reserve[2]" escape="false"/> <s:property value="productPagerBean.reserve[3]" escape="false"/> <s:property value="productPagerBean.reserve[4]" escape="false"/>",
	</s:if>
	"total":<s:property value="productPagerBean.count"/>,
	"rows":[
	<s:iterator value="productPagerBean.dataList" status="st">
	{
	<s:iterator value="json" status="mapst">
	"<s:property value="key" escape="false"/>":"<s:property value="value" escape="false"/>"<s:if test="not #mapst.last">,</s:if>
	</s:iterator>
	}<s:if test="not #st.last">,</s:if>
	</s:iterator>
	]
}