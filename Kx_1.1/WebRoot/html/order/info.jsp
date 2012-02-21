<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
{
"orderType":<s:property value="order.json()" escape="false"/>,
"feedbackList":<s:property value="feedbackBean.json" escape="false"/>
}