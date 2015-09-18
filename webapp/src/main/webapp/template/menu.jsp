<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.commons.PermissionConstants"%>




	<td height="100%">

	<!--                      -->
	<!-- left-side menu table -->
	<!--                      -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		    <tr valign="top">
		        <td height="15">
	                <table width="100%" class="table" height="15" align="center" cellpadding="0" cellspacing="0" >
				        <tr>
	                        <td class="menu-title">
<logic:present name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session">
                                <bean:message key="text.menuTitle" bundle="general"/>
</logic:present>
                                &nbsp;
		                    </td>
		                </tr>
	                </table>
	            </td>
	        </tr>
	        <tr>
                <td height="2"></td>
	        </tr>
	        <tr valign="top">
                <td>
	                <table class="table" width="100%" height="100%" align="top"  cellpadding="0" cellspacing="0" >
	    	            <tr>
	                        <td height="5" ></td>
            		    </tr>

<logic:present name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session">

	<bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session" type="br.com.auster.security.model.User"/>

		                <tr valign="middle" onMouseOver="this.style.background='#f0f0f0';"onMouseOut="this.style.background='#ffffff';">
	 	                   	<td height="25">
     	                   		<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
                        		<html:link page="/list-requests.do"><bean:message key="text.requestMenuItem" bundle="general"/></html:link>
                        		</span>
                        	</td>
		                </tr>

	<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_REQUEST_CREATE_KEY)) { %>
        		        <tr valign="middle"  onMouseOver="this.style.background='#f0f0f0';"onMouseOut="this.style.background='#ffffff';">
                        	<td height="25">
                        		<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
                        		<html:link page="/requests/upload.jsp"><bean:message key="text.uploadMenuItem" bundle="general"/></html:link>
                        		</span>
                        	</td>
		                </tr>
    <% } %>
		                <tr valign="middle"  onMouseOver="this.style.background='#f0f0f0';"onMouseOut="this.style.background='#ffffff';">
	                        <td height="25">
	                        	<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
		                        <html:link action="/list-users.do"><bean:message key="text.userMgmtMenuItem" bundle="general"/></html:link>
		                        </span>
							</td>
		                </tr>

	<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_IQ_SEARCH_KEY)) { %>		                
		                
		              	<tr>
  		              	    <td height="25">
  		              	        <span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
							    <html:link page="/reports/queries.jsp"><bean:message key="text.dynQueriesTableTitle" bundle="general"/></html:link>
							    </span>
						    </td>
		              	</tr>

	<% } %>

		<tr>
			<td height="55">
				<span class="textBlue">&nbsp;
					<bean:message key="text.customMenuItem" bundle="custom"/>
				</span>
			</td>
		</tr>
	
	<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT) ||
		   user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_VIEW)) { %>


						<tr>
							<td height="25">
								<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
									<html:link action="/carrier.do"><bean:message key="text.carriersMgmtMenuItem" bundle="general"/></html:link>
								</span>
							</td>
						</tr>
						<tr>
							<td height="25">
								<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
									<html:link action="/cycles.do"><bean:message key="text.cycleDatesMgmtMenuItem" bundle="general"/></html:link>
								</span>
							</td>
						</tr>
						<tr>
							<td height="25">
								<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
									<html:link action="/customer.do"><bean:message key="text.customerTypeMgmtMenuItem" bundle="general"/></html:link>
								</span>
							</td>
						</tr>
						<tr>
							<td height="25">
								<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
									<html:link action="/promotionPackages.do"><bean:message key="text.promotionPackageMenuItem" bundle="custom"/></html:link>
								</span>
							</td>
						</tr>
						<tr>
							<td height="25">
								<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
									<html:link action="/mandatoryPlans.do"><bean:message key="text.plansMenuItem" bundle="custom"/></html:link>
								</span>
							</td>
						</tr>
						<tr>
							<td height="25">
								<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
									<html:link action="/progressiveDiscount.do"><bean:message key="text.progressiveDiscountMenuItem" bundle="custom"/></html:link>
								</span>
							</td>
						</tr>
						<tr>
							<td height="25">
								<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
									<html:link action="/exclusivePromotion.do"><bean:message key="text.exclusivePromotionMenuItem" bundle="custom"/></html:link>
								</span>
							</td>
						</tr>
						<tr>
							<td height="25">
								<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
									<html:link action="/list-nf-thresholds.do"><bean:message key="text.NFThresholdMgmtMenuItem" bundle="general"/></html:link>
								</span>
							</td>
						</tr>
						<tr>
							<td height="25">
								<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
									<html:link action="/searchTariff.do"><bean:message key="text.SearchTariffMenuItem" bundle="tariff"/></html:link>
								</span>
							</td>
						</tr>

	<% }

	   if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_BILLINGVIEW)) { %>


						<tr>
							<td height="25">
								<span class="textBlue">&nbsp;<html:img page="/images/box.gif" width="6" height="6"/>&nbsp;
									<html:link action="/searchParameters.do"><bean:message key="text.bscsIntegrationMenuItem" bundle="custom"/></html:link>
								</span>
							</td>
						</tr>
	<% } %>



</logic:present>


<logic:notPresent name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session">
		              	<tr>
							<td height="25">&nbsp;</td>
		              	</tr>
		              	<tr>
		                	<td height="25">&nbsp;</td>
		              	</tr>
		              	<tr>
		                	<td height="25">&nbsp;</td>
		              	</tr>
		              	<tr>
		                	<td height="25">&nbsp;</td>
		              	</tr>
</logic:notPresent>
                		<tr valign="bottom">
                        	<td><html:img page="/images/menu.jpg"/></td>
		                </tr>
	                </table>
    			</td>
			</tr>
        </table>
	</td>

