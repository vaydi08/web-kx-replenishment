<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
{
	"success":<s:property value="result.resultSuccess"/>,
	"msg":"<s:property value="result.resultErrMsg" escape="false"/>"
}