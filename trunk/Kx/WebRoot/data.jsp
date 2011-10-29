<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
{
	"total":<s:property value="pagerBean.count"/>,
	"reserve":[
	<s:iterator value="pagerBean.reserve" status="st">
	"<s:property escape="false"/>"<s:if test="not #st.last">,</s:if>
	</s:iterator>
	],
	"rows":[
	<s:iterator value="pagerBean.dataList" status="st">
	{
	<s:iterator value="json" status="mapst">
	"<s:property value="key" escape="false"/>":"<s:property value="value" escape="false"/>"<s:if test="not #mapst.last">,</s:if>
	</s:iterator>
	}<s:if test="not #st.last">,</s:if>
	</s:iterator>
	]
	<s:if test="pagerBean.hasReserve()">
	,"footer":[{"id":"未处理订单:","ordertime":"<s:property value="pagerBean.reserve[0]"/>"},
	{"id":"我正处理的订单:","ordertime":"<s:property value="pagerBean.reserve[1]"/>"},
	{"id":"订购期限小于7天的订单:","ordertime":"<s:property value="pagerBean.reserve[2]"/>"}]
	</s:if>
}