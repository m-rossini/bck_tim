<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>


<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.PermissionConstants"%>
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


<script language="javascript">

function submitForm() {

	form = document.forms[0];
	if (form.groupName.value.length <= 0) {
		window.alert("<bean:message key="text.groups.nameMustExist" bundle="users"/>");
		return;
	}
	form.submit();
}

</script>

	<td>
	<table width="100%" height="100%" cellpadding="0" cellspacing="0">
	   <tr valign="top">
        	       <td height="15">
        		<table class="table" width="100%" height="100%" align="center" cellpadding="0" cellspacing="0">
	           <tr>
                	   <td height="100%" class="table-title"><bean:message key="text.createGroupTableTitle" bundle="users"/></td>
		 </tr>
            	</table>
                 </td>
                 </tr>
		<tr valign="top">
        	           <td height="2"></td>
                    </tr>
		<tr  valign="top">
		  <td  height="100%"width="100%">

<html:form action="/create-group" method="post">
			<table width="100%" height="100%" align="center" class="table" cellpadding="3" cellspacing="0">
			 <tr height="30">
  	   		    <td colspan="6"></td>
			 </tr>

    		 <tr height="20">
          		<td colspan="6" class="text8">
          			<div align="left">
          			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          			<font class="textBlue"><bean:message key="text.groupNameColumn" bundle="users"/></font>
          			<html:text property="groupName" size="40" maxlength="120" styleClass="mandatory-input-field"/>
          		</td>
  			 </tr>
  			 <tr height="25">
	   		     <td colspan="6" class="text8bRed">
	   		     	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   		     	<html:errors bundle="users" property="groupName"/>
	   		     </td>
			</tr>

    		<tr height="20">
				<td width="20%" class="textBlue"> <div align="left"><bean:message key="text.viewRequestsPermissionsColumn" bundle="users"/></div></td>
       			<td width="10%" class="text8"><div align="left"><html:multibox	property="permissionId" value="<%=PermissionConstants.PERMISSION_REQUEST_GROUPVIEW_KEY%>"/></div></td>
       			<td width="20%" class="textBlue"> <div align="left"><bean:message key="text.createRequestPermissionsColumn" bundle="users"/></div></td>
       			<td width="10%" class="text8"><div align="left"><html:multibox	property="permissionId" value="<%=PermissionConstants.PERMISSION_REQUEST_CREATE_KEY%>"/></div></td>
       			<td width="20%" class="textBlue"> <div align="left"><bean:message key="text.iqPermissionColumn" bundle="users"/></div></td>
       			<td width="10%" class="text8"><div align="left"><html:multibox	property="permissionId" value="<%=PermissionConstants.PERMISSION_IQ_SEARCH_KEY%>"/></div></td>
			</tr>

    		<tr height="20">
       			<td class="textBlue"> <div align="left"><bean:message key="text.userCreatePermissionColumn" bundle="users"/></div></td>
       			<td class="text8"><div align="left"><html:multibox	property="permissionId" value="<%=PermissionConstants.PERMISSION_USER_UPDATE_OTHERS_KEY%>"/></div></td>
       			<td class="textBlue"> <div align="left"><bean:message key="text.groupViewPermissionColumn" bundle="users"/></div></td>
       			<td class="text8"><div align="left"><html:multibox	property="permissionId" value="<%=PermissionConstants.PERMISSION_USER_GOUPS_VIEW_KEY%>"/></div></td>
       			<td class="textBlue"> <div align="left"><bean:message key="text.groupCreatePermissionColumn" bundle="users"/></div></td>
       			<td class="text8"><div align="left"><html:multibox	property="permissionId" value="<%=PermissionConstants.PERMISSION_USER_GOUPS_CREATE_KEY%>"/></div></td>
			</tr>

            <tr height="20">
                <td class="textBlue"> <div align="left"><bean:message key="text.managerRequestPermissionColumn" bundle="users"/></div></td>
                <td class="text8"><div align="left"><html:multibox    property="permissionId" value="<%=PermissionConstants.PERMISSION_MANAGER_REQUEST_KEY%>"/></div></td>
                <td class="textBlue"> <div align="left"><bean:message key="text.customViewPermissionColumn" bundle="usersCustom"/></div></td>
                <td class="text8"><div align="left"><html:multibox    property="permissionId" value="<%=PermissionConstants.PERMISSION_ADVANCED_RULECFG_VIEW%>"/></div></td>
                <td class="textBlue"> <div align="left"><bean:message key="text.customEditPermissionColumn" bundle="usersCustom"/></div></td>
                <td class="text8"><div align="left"><html:multibox    property="permissionId" value="<%=PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT%>"/></div></td>
            </tr>

            <tr height="20">
                <td class="textBlue"> <div align="left"><bean:message key="text.billingIntegrationPermissionColumn" bundle="usersCustom"/></div></td>
                <td class="text8"><div align="left"><html:multibox    property="permissionId" value="<%=PermissionConstants.PERMISSION_ADVANCED_BILLINGVIEW%>"/></div></td>
            </tr>

  			<tr height="15">
			     <td colspan="6">&nbsp;</td>
			</tr>

  			<tr height="25" valign="middle">
  	   		    <td colspan="6" class="textBlue">
  	   		        <div align="right">
  	         			<html:link href="javascript:submitForm();">:: <bean:message key="text.save" bundle="users"/> ::</html:link>&nbsp;&nbsp;&nbsp;&nbsp;
  	         			<html:link href="javascript:history.back();">:: <bean:message key="text.cancel" bundle="users"/> ::</html:link>&nbsp;&nbsp;&nbsp;&nbsp;
  	   		        </div>
  	   		    </td>
			</tr>
</html:form>
		</table>
		</td>
   		</tr>
   		</table>
   	</td>
