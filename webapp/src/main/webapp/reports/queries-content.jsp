<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>


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


	<td height="100%">
		<table width="100%"  height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<td width="100%" height="15" class="table-title"><bean:message key="text.dynQueriesTableTitle" bundle="general"/></td>
		</tr>
        <tr><td height="2"></td></tr>
		<tr valign="top">
			<td  height="100%" width="100%">
				<table width="100%" height="100%" class="table" cellpadding="1" cellspacing="0">
					<tr class="textBlue" height="30%">
						<td width="2%"></td>
						<td width="32%"> <a href="javascript:submitQueryForm('invoice')">Totais de Faturas</a> </td>
						<td width="32%"> <a href="javascript:submitQueryForm('rateplan')">Totais por Plano</a> </td>
						<td width="32%"> <a href="javascript:submitQueryForm('carrier')">Totais por Operadora</a> </td>
						<td width="2%"></td>
					</tr>
					<tr height="100%">
					</tr>
				</table>
			</td>
		</tr>
		</table>
	</td>

<form name="query" method="post" action="<html:rewrite page="/reports/displayQuery.jsp"/>">
	<input type="hidden" name="query" value=""/>
</form>

<script>
function submitQueryForm(queryName) {
	document.forms['query'].query.value = queryName;
	document.forms['query'].submit();
}
</script>
