<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<html:html locale="true">


<head>
  <title>:::: <bean:message key="text.title" bundle="general"/> ::::</title>
  <link href="<html:rewrite page="/css/data_general.css"/>"      rel="stylesheet" type="text/css">
  <link href="<html:rewrite page="/css/data.css"/>"              rel="stylesheet" type="text/css">
  <link href="<html:rewrite page="/jpivot/table/mdxtable.css"/>" rel="stylesheet" type="text/css" >
  <link href="<html:rewrite page="/jpivot/navi/mdxnavi.css"/>"   rel="stylesheet" type="text/css">
  <link href="<html:rewrite page="/wcf/form/xform.css"/>"        rel="stylesheet" type="text/css">
  <link href="<html:rewrite page="/wcf/table/xtable.css"/>"      rel="stylesheet" type="text/css">
  <link href="<html:rewrite page="/wcf/tree/xtree.css"/>"        rel="stylesheet" type="text/css">
  <meta http-equiv="refresh" content="1; URL=<c:out value="${requestSynchronizer.resultURI}"/>">
</head>


<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>

<auster:checkLogon
   	    sessionKey="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>">
<body>
	<form name="redirectForm" action="<html:rewrite page="/errorPage.do"/>" method="post">
		<input type="hidden" name="<%=ExceptionConstants.USERNOTLOGGED_KEY%>" value="true"/>
	</form>
</body>
<script language="javascript">
	document.forms['redirectForm'].submit();
</script>			
			
</auster:checkLogon>



<body>
<table width="100%" height="100%" cellspacing="2" cellpadding="0">
	<tr>
	<td height="100%">
		<table width="100%"  height="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td width="100%" height="15" class="table-title"><bean:message key="text.dynQueriesTableTitle" bundle="general"/></td>
		</tr>
        <tr><td height="2"></td></tr>
		<tr>
			<td width="100%" class="text8"><bean:message key="text.click" bundle="requests"/>
			                 <a class="textBlue" href="<c:out value="${requestSynchronizer.resultURI}"/>"><bean:message key="text.here" bundle="requests"/></a>  
			                 <bean:message key="text.noRedirectOnBrowser" bundle="general"/></td>
		</tr>
		</table>
	</td>
	</tr>
</table>
 
</body>
</html:html>