<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<%@ page import="br.com.auster.dware.console.commons.PermissionConstants"%>
<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
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

	<td>

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->
		<bean:define id="promotionInfo" name="<%=RequestScopeConstants.REQUEST_REQID_KEY%>" scope="request"/>
		<table width="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
				<TD widht="100%">
					<!-- if exists packageInfo, then list them -->
					<TABLE class="table" width="100%">
						<TR class="table-title">
							<TD colspan="4"><bean:message key="title.confirmation.operation" bundle="promotions"/></TD>
						</TR>
						<TR height="60">
							<TD width="10%"/>
							<TD colspan="2" class="text8" align="left">
								<bean:message key="message.result.preFix" bundle="promotions"/>
								( <bean:write name="promotionInfo" property="shortDesc"/> ) <bean:write name="promotionInfo" property="description"/>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
								<bean:message key="message.operation.enclosed" bundle="promotions"/>
</logic:equal>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE%>">
								<bean:message key="message.operation.modified" bundle="promotions"/>
</logic:equal>
<logic:equal name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>">
								<bean:message key="message.operation.excluded" bundle="promotions"/>
</logic:equal>
								<bean:message key="message.result.suffix" bundle="promotions"/>
								<BR/>
								<BR/>
								<A href="<html:rewrite page="/exclusivePromotion.do"/>"><bean:message key="clickHere.return" bundle="promotions"/></A>
							</TD>
							<TD width="10%"/>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</TD>