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


  <bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"  type="br.com.auster.security.model.User"/>

  <bean:define id="ongoingOperation" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request"/>
  <bean:define id="currentPromotion" name="<%=RequestScopeConstants.REQUEST_REQID_KEY %>" type="br.com.auster.tim.billcheckout.model.ExclusivePromotion"/>

<script language="Javascript">
function submitForm() {
	if (validatePromotionForm(document.promotionForm)) {
		document.promotionForm.submit();
	}
}

function openWindow() {
	window.open("<html:rewrite page="/promotionsPopup.do"/>?uid=<bean:write name="currentPromotion" property="uid"/>", "promotionsPopup", "scrollbars,resizable,width=500,height=600");
	return;
}
</script>

   <html:javascript formName="promotionForm"/>



  <html:form action="/exclusivePromotion" method="post" id="promotionForm">
	<html:hidden property="uid" value=""/>
  		<script>
  			document.forms['promotionForm'].uid.value='<bean:write name="currentPromotion" property="uid"/>';
  		</script>

	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="<%=ongoingOperation%>"/>


  <td widht="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
          	<td height="15">
          		<table class="table" width="100%" height="100%" cellpadding="0" cellspacing="0">
	              	<tr>
					<logic:equal name="ongoingOperation" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
	                	<td colspan="4" height="100%" class="table-title" align="right"><bean:message key="text.addPromotionTableTitle" bundle="promotions"/></td>
	                </logic:equal>
					<logic:equal name="ongoingOperation" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE%>">
	                	<td colspan="4" height="100%" class="table-title" align="right"><bean:message key="text.updatePromotionTableTitle" bundle="promotions"/></td>
	                </logic:equal>
	              	</tr>
              	</table>
            </td>
       	</tr>
       	<tr valign="top">
 			<td height="2"></td>
 		</tr>

		<tr valign="top">
			<td  height="100%" width="100%">
				<table width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">

			  	<tr height="20" class="table-column-title">
			    	<td width="15%"><div align="left"><bean:message key="text.promotionCode" bundle="promotions"/></div></td>
			    	<td colspan="3"><div align="left"><bean:message key="text.promotionDescription" bundle="promotions"/></div></td>
                </tr>

	          <tr height="20" class="text8" align="left" valign="top">

	          	<td width="10%">
	          	<logic:equal name="ongoingOperation" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
	          		<html:text styleClass="mandatory-input-field" property="shortDesc" size="8" maxlength="16" tabindex="1" value=""/>
		          		<script>
		          			document.forms['promotionForm'].shortDesc.value='<bean:write name="currentPromotion" property="shortDesc"/>';
		          		</script>
	          	</logic:equal>
	          	<logic:equal name="ongoingOperation" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE%>">
	          		<html:text styleClass="mandatory-input-field" readonly="true" property="shortDesc" size="8" maxlength="16" tabindex="1" value=""/>
		          		<script>
		          			document.forms['promotionForm'].shortDesc.value='<bean:write name="currentPromotion" property="shortDesc"/>';
		          		</script>
	          	</logic:equal>
	            </td>
	            <td width="40%"><html:text styleClass="mandatory-input-field" property="description" size="40" maxlength="64" tabindex="2" value=""/></td>
		          		<script>
		          			document.forms['promotionForm'].description.value='<bean:write name="currentPromotion" property="description"/>';
		          			<% if (!user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
		          			document.forms['promotionForm'].description.disabled=true;
		          			<% } %>
		          		</script>

	            <!--  save, hidden, the list of UIDs for current package -->

	            <td colspan="2">
	            	<logic:equal name="ongoingOperation" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE%>">
	            	<a href="javascript:openWindow()"><bean:message key="text.assocPromotions" bundle="promotions"/></a>
	            	</logic:equal>
	            </td>
	          </tr>

				<tr>
			    	<td class="text8" height="30" colspan="4" align="right">
			    			<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
							<a href="javascript:submitForm()">:: <bean:message key="text.confirm" bundle="promotions"/> ::</a> &nbsp;&nbsp;
		          			<% } %>
							<a href="<html:rewrite page="/exclusivePromotion.do"/>">:: <bean:message key="text.cancel" bundle="promotions"/> ::</a>  &nbsp;&nbsp;&nbsp;&nbsp;
			    	</td>
			    </tr>

				</table>
			</td>
   		</tr>
   		</table>
   	</td>

  </html:form>
