<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>
<%@ page import="br.com.auster.tim.billcheckout.portal.tariff.TariffPagesConstants"%>

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
		function submitForm(){
			document.searchTariffForm.submit();
			return;
		}
	</script>

  <html:form action="/searchTariff" method="post">
	<logic:present name="<%=TariffPagesConstants.SELECTED_PLAN_UF%>" scope="request">
	  <html:hidden property="operation" value="listVoice"/>
	</logic:present>	

  <td height="100%">

      <bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"  type="br.com.auster.security.model.User"/>
  
        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
			<tr valign="top">
	          	<td height="15">
	          		<table class="table" width="100%" height="100%" cellpadding="0" cellspacing="0">
		              	<tr>
		                	<td colspan="4" height="100%" class="table-title" align="right"><bean:message key="text.searchTariffTableTitle" bundle="tariff"/></td>
		              	</tr>
	              	</table>
	            </td>
	       	</tr>
	       	
	       	<tr valign="top">
	 			<td height="2"></td>
	 		</tr>
			<tr valign="top">
				<td width="100%" height="100%">
				<table class="table" align="center" cellpadding="3" cellspacing="0" width="100%" height="100%">
					<tr height="20">
				    	<td>
				    		<div align="left" class="text8b">
				    		
								<!-- 
								This combo is displayed if there is no UF selected yet 
								-->
				    			<bean:message key="text.selectUf" bundle="tariff"/>:
		
								<logic:present name="<%=TariffPagesConstants.SELECTED_PLAN_UF%>" scope="request">
								   <bean:write name="planUF" scope="request"/>
								</logic:present>
		
				    			<logic:notPresent name="<%=TariffPagesConstants.SELECTED_PLAN_UF%>" scope="request">
					    			<bean:define name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" id="allUfs" scope="request"/>
					    			<html:select property="<%=TariffPagesConstants.SELECTED_PLAN_UF%>" styleClass="input-field" tabindex="1">
						    			<html:options name="allUfs"/>
									</html:select>
								</logic:notPresent>
				    		</div>
							<br/>
				    		<div align="left"  class="text8b">
		
								<!--
									If there is any UF selected, then we hide de UF combo and display the rateplan combo,
										based on the UF selected. 
								-->
		
								<logic:present name="<%=TariffPagesConstants.SELECTED_PLAN_UF%>" scope="request">
											    			
					    			<bean:message key="text.selectRateplan" bundle="tariff"/>:
		
									<bean:define name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" id="allRateplans" scope="request"/>
					    			<html:select property="<%=TariffPagesConstants.SELECTED_RATE_PLAN%>" styleClass="input-field" tabindex="1">
						    			<logic:empty name="allRateplans">
						    				<html:option value="">Selecione</html:option>
						    			</logic:empty>
						    			<logic:iterate id="listPlan" name="allRateplans">
											<option value="<bean:write name='listPlan' property='uid'/>-<bean:write name='listPlan' property='planName'/>-<bean:write name='listPlan' property='state'/>">
												<bean:write name="listPlan" property="planName"/>
											</option>
										</logic:iterate>
					    			</html:select>
					    		</logic:present>
				    		</div>
				    	</td>
			   		</tr>
			        <tr valign="bottom">
				    	<td class="text8" height="30" align="right">
			    			<a href="javascript:submitForm()">:: <bean:message key="text.viewTariffs" bundle="tariff"/> ::</a>  &nbsp;&nbsp;&nbsp;&nbsp;
				    	</td>
				    </tr>
				    <tr><td>&nbsp;</td></tr>
				</table>
				</td>
			</tr>
   		</table>
  </td>
  </html:form>
  
  