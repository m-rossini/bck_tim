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
<bean:define id="currentDiscountTable" name="<%=RequestScopeConstants.REQUEST_REQID_KEY%>" scope="request"/>
<bean:define id="currentDiscountsRange" name="currentDiscountTable" property="sortedDiscountRates"/>
<bean:define id="discountUid" name="currentDiscountTable" property="uid"/>
<bean:define id="discountDescUid" name="discountDescUid" scope="request"/>

<script language="Javascript">
	function submitForm(objId, type) {
		document.forms['discountTableForm'].elements['<%=RequestScopeConstants.REQUEST_REQID_KEY%>'].value = objId;
		document.forms['discountTableForm'].elements['<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>'].value = type;

		if (type == '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>') {
			if (!confirm('<bean:message key="message.sureToEraseDiscRange" bundle="progressiveDiscount"/> ' + '?')) {
				return;
			};
		}
		document.forms['discountTableForm'].submit();
	}

	function save() {
		if (validateDiscountTableForm(document.discountTableForm)) {
			document.discountTableForm.submit();
		}
	}

	function openWindow(uid) {

		if (validateDiscountTableForm(document.discountTableForm)) {
			if(document.forms['discountTableForm'].elements['<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>'].value == 'insert' ) {
				if(confirm('<bean:message key="message.addRateAlert" bundle="progressiveDiscount"/> ' + '?')) {
					document.forms['discountTableForm'].elements['<%=RequestScopeConstants.REQUEST_REQID_KEY%>'].value = 'new';
					document.forms['discountTableForm'].submit();
				} else {
					return;
				}
			}
		} else {
			return;
		}

		window.open("<html:rewrite page="/discountRangePopup.do"/>?uid="+uid+"&discountDescUid=<bean:write name="discountDescUid"/>&discountUid=<bean:write name="discountUid"/>", "plansPopup", "scrollbars,resizable,width=500,height=180");
		return;
	}

	function openPlansWindow() {
		window.open("<html:rewrite page="/discountTablePlansPopup.do"/>?discountDescUid=<bean:write name="discountDescUid"/>&discountUid=<bean:write name="discountUid"/>", "plansPopup", "scrollbars,resizable,width=500,height=600");
		return;
	}

</script>


	  <td widht="100%">

	      <html:javascript formName="discountTableForm"/>

	  	  <html:form action="/discountTable" method="post" id="discountTableForm">
			<input type=hidden name="<%=RequestScopeConstants.REQUEST_REQID_KEY%>"/>
			<input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="<%=ongoingOperation%>"/>
			<html:hidden property="discountDescUid" value=""/>
		        <script>
         			document.forms['discountTableForm'].discountDescUid.value='<bean:write name="discountDescUid"/>';
         		</script>
			<html:hidden property="discountUid" value=""/>
				<script>
         			document.forms['discountTableForm'].discountUid.value='<bean:write name="discountUid"/>';
         		</script>

	        <!--                          -->
	        <!-- right-side content table -->
	        <!--                          -->

			<table width="100%" cellpadding="0" cellspacing="0">
				<tr valign="top">
		          	<td height="15">
		          		<table class="table" width="100%" cellpadding="0" cellspacing="0">
			              	<tr>
							<logic:equal name="ongoingOperation" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
			                	<td colspan="4" height="100%" class="table-title" align="right"><bean:message key="text.addDiscountTable" bundle="progressiveDiscount"/></td>
			                </logic:equal>
							<logic:equal name="ongoingOperation" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE%>">
			                	<td colspan="4" height="100%" class="table-title" align="right"><bean:message key="text.viewEditDiscountTable" bundle="progressiveDiscount"/></td>
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
					    	<td width="50%"><div align="left"><bean:message key="text.discountTableName" bundle="progressiveDiscount"/></div></td>
							<td width="50%"></td>
		                </tr>

			            <tr height="20" class="text8" align="left" valign="top">
				          	<td>
				          		<html:text styleClass="mandatory-input-field" property="rangeName" size="25" maxlength="50" tabindex="1" value=""/>
				          		<script>
				          			document.forms['discountTableForm'].rangeName.value='<bean:write name="currentDiscountTable" property="rangeName"/>';
				          			<% if (!user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
				          				document.forms['discountTableForm'].rangeName.disabled=true;
				          			<% } %>
				          		</script>
				            </td>
				            <td align="right" width="50%"><a href="javascript:openPlansWindow()"><bean:message key="text.assocPlans" bundle="packages"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			            </tr>

						<tr height="30">
							<td></td>
							<td></td>
						</tr>

						<tr height="20" class="table-column-title">
					    	<td colspan="2"><div align="center"><bean:message key="text.discountsRange" bundle="progressiveDiscount"/></div></td>
		                </tr>

					    <tr height="20" class="text8" align="left" valign="top">
					    	<td colspan="2">
							<table width="100%" class="table">
								<tr>
		                          <td align="left" width="25%" class="table-column-title"><bean:message key="text.lowerLimit" bundle="progressiveDiscount"/></td>
		                          <td align="left" width="25%" class="table-column-title"><bean:message key="text.upperLimit" bundle="progressiveDiscount"/></td>
		                          <td align="left" width="25%" class="table-column-title"><bean:message key="text.discountRate" bundle="progressiveDiscount"/></td>
								  <td align="left" width="15%" class="table-column-title"><bean:message key="text.remove" bundle="progressiveDiscount"/></div></td>
		                        </tr>
		                        <logic:notEmpty name="currentDiscountsRange">
									<logic:iterate id="discountRange" name="currentDiscountsRange">
				                        <tr>
				                          <td class="text8"><div align="center">
				                          		<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
				                          		<a href="javascript:openWindow('<bean:write name="discountRange" property="uid"/>')">
				                          		<% } %>
	      											<bean:write name="discountRange" property="lowerLimit" format="###,###,##0.00"/>
	      										<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
	      										</a>
	      										<% } %>
	      									</div>
	      								  </td>
			                          	  <td class="text8"><div align="center">
			                          	  		<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
			                          	  		<a href="javascript:openWindow('<bean:write name="discountRange" property="uid"/>')">
			                          	  		<% } %>
				                          			<bean:write name="discountRange" property="upperLimit" format="###,###,##0.00"/>
				                          		<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
				                          		</a>
				                          		<% } %>
				                          	</div>
	      								  </td>
			                          	  <td class="text8"><div align="center">
			                          	  		<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
			                          	  		<a href="javascript:openWindow('<bean:write name="discountRange" property="uid"/>')">
			                          	  		<% } %>
				                          			<bean:write name="discountRange" property="discountRate" format="###,###,##0.00"/>
				                          		<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
				                          		</a>
				                          		<% } %>
				                          	</div>
	      								  </td>
				                          <td><div align="center">
				                          	<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
											<a href="javascript:submitForm('<bean:write name="discountRange" property="uid"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>')">
												<img src="images/no.jpg" border="0"/>
											</a>
											<% } %>
											</div>
										  </td>
				                        </tr>
		                        	</logic:iterate>
								</logic:notEmpty>
		                     </table>
		                     </td>
				        </tr>

				        <tr>
					    	<td class="text8" height="30" colspan="2" align="right">
					    		<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
									<a href="javascript:openWindow('')"><bean:message key="text.addDiscountRange" bundle="progressiveDiscount"/></a> &nbsp;&nbsp;
								<% } %>
					    	</td>
					    </tr>

				       <tr height="50">
				       	<td></td>
				       	<td></td>
				       </tr>

				       <tr>
					    	<td class="text8" height="30" colspan="2" align="right">
					    			<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
									<a href="javascript:save()">:: <bean:message key="text.confirm" bundle="progressiveDiscount"/> ::</a> &nbsp;&nbsp;
									<% } %>
									<a href="<html:rewrite page="/progressiveDiscount.do"/>?<%=RequestScopeConstants.REQUEST_REQID_KEY%>=<bean:write name="discountDescUid"/>&operation=detail">:: <bean:message key="text.cancel" bundle="progressiveDiscount"/> ::</a>  &nbsp;&nbsp;&nbsp;&nbsp;
					    	</td>
					    </tr>

						</table>
					</td>
		   		</tr>

			</table>
	  </html:form>
	   	</td>


