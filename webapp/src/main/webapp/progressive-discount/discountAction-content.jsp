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
  <bean:define id="currentDiscount" name="<%=RequestScopeConstants.REQUEST_REQID_KEY%>" type="br.com.auster.tim.billcheckout.model.ProgressiveDiscountDesc"/>
  <bean:define id="currentDiscountTable" name="currentDiscount" property="progDiscount"/>
  <bean:define id="currentDiscountTableUid" name="currentDiscountTable" property="uid"/>
  <bean:define id="allDiscountsTables"  name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>
  <bean:define id="allUfs"  name="request.uflist" scope="request"/>

<script language="Javascript">
function submitForm() {
	if (validateDiscountForm(document.discountForm)) {
		document.discountForm.submit();
	}
}

function submitDiscTableForm(value) {
	if(value == 'uid') {
		document.discountTableForm.discountUid.value = document.discountForm.discountsTables.value;
	} else {
		document.discountTableForm.discountUid.value = '';
	}
	document.discountTableForm.submit();
}

</script>

   <html:javascript formName="discountForm"/>

  <html:form action="/discountTable" method="post" id="discountTableForm">
	<html:hidden property="discountUid"/>
	<html:hidden property="discountDescUid" value="<%=String.valueOf(currentDiscount.getUid())%>"/>
  </html:form>

  <html:form action="/progressiveDiscount" method="post" id="discountForm">
	<html:hidden property="uid" value="<%=String.valueOf(currentDiscount.getUid())%>"/>
	<html:hidden property="discountTableUid" value="<%=String.valueOf(currentDiscountTableUid)%>"/>

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
	                	<td colspan="4" height="100%" class="table-title" align="right"><bean:message key="text.addDiscountTableTitle" bundle="progressiveDiscount"/></td>
	                </logic:equal>
					<logic:equal name="ongoingOperation" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE%>">
	                	<td colspan="4" height="100%" class="table-title" align="right"><bean:message key="text.updateDiscountTableTitle" bundle="progressiveDiscount"/></td>
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
			    	<td width="50%"><div align="left"><bean:message key="text.discountDescTableTitle" bundle="progressiveDiscount"/></div></td>
			    	<td width="50%"><div align="left"><bean:message key="text.discountTableTitle" bundle="progressiveDiscount"/></div></td>
			    	<td width="50%"><div align="left"><bean:message key="text.discountDescUf" bundle="progressiveDiscount"/></div></td>
                </tr>

	            <tr height="20" class="text8" align="left" valign="top">

		          	<td width="40%">
		          		<html:text styleClass="mandatory-input-field" property="discountDesc" size="25" maxlength="50" tabindex="1" value="<%=currentDiscount.getDiscountDesc()%>"/>
		          		<% if (!user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
		          		<script language="Javascript">
		          			document.forms['discountForm'].discountDesc.disabled=true;
		          		</script>
		          		<% } %>
		            </td>
		            <td width="40%">
						 <html:select property="discountsTables" styleClass="input-field" tabindex="2">
	                     	<logic:notEmpty name="allDiscountsTables">
								<html:optionsCollection name="allDiscountsTables" value="uid" label="rangeName" />
							</logic:notEmpty>
	                	</html:select>
		            </td>

		            <script language="Javascript">
						for(i=0;i<=document.discountForm.discountsTables.options.length;i++) {
							if (document.discountForm.discountsTables.options[i].value == document.discountForm.discountTableUid.value) {
								document.discountForm.discountsTables.selectedIndex = i;
								break;
							}
						}

						<% if (!user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
							document.forms['discountForm'].discountsTables.disabled=true;
						<% } %>
	 				</script>

		            <td width="20%">
						 <html:select property="discountsDescUF" styleClass="input-field" tabindex="3">
	                     	<logic:notEmpty name="allUfs">
								<html:options name="allUfs"/>
							</logic:notEmpty>
	                	</html:select>
		            </td>

		            <script language="Javascript">
	            	   	var state = '<bean:write name="currentDiscount" property="state"/>';
	     					if(state == null || state == '') {
	     						state = 'Todos';
	     					}
						for(i=0;i<=document.discountForm.discountsDescUF.options.length;i++) {
							if (document.discountForm.discountsDescUF.options[i].value == state) {
								document.discountForm.discountsDescUF.selectedIndex = i;
								break;
							}
						}

						<% if (!user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
							document.forms['discountForm'].discountsDescUF.disabled=true;
						<% } %>
	 				</script>

	            </tr>

			  	<tr>
			  		<td class="text8" height="30" colspan="4" align="right">
			  			<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
			    		<a href="javascript:submitDiscTableForm('')">:: <bean:message key="text.addDiscountTable" bundle="progressiveDiscount"/> ::</a> &nbsp;&nbsp;
			    		<% } %>
			    		<a href="javascript:submitDiscTableForm('uid')">:: <bean:message key="text.viewEditDiscountTable" bundle="progressiveDiscount"/> ::</a> &nbsp;&nbsp;
			    	</td>
                </tr>

				<tr height="30">

				<tr height="20" class="table-column-title">
			    	<td width="50%"><div align="left"><bean:message key="text.discountDescShdes" bundle="progressiveDiscount"/></div></td>
                </tr>

			    <tr height="20" class="text8" align="left" valign="top">
		        	<td width="50%">
		          		<html:text styleClass="input-field" property="serviceShortDesc" size="15" maxlength="30" tabindex="4" value="<%=currentDiscount.getServiceShortDesc()%>"/>
		          		<% if (!user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
		          		<script language="Javascript">
		          			document.forms['discountForm'].serviceShortDesc.disabled=true;
		          		</script>
		          		<% } %>
		            </td>
		        </tr>

		       <tr height="50"/>

		       <tr>
			    	<td class="text8" height="30" colspan="4" align="right">
			    		<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
						<a href="javascript:submitForm()">:: <bean:message key="text.confirm" bundle="progressiveDiscount"/> ::</a> &nbsp;&nbsp;
						<% } %>
						<a href="<html:rewrite page="/progressiveDiscount.do"/>">:: <bean:message key="text.cancel" bundle="progressiveDiscount"/> ::</a>  &nbsp;&nbsp;&nbsp;&nbsp;
			    	</td>
			    </tr>

				</table>
			</td>
   		</tr>
   		</table>
   	</td>

  </html:form>
