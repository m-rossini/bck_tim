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


	<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" id="filterValue" type="java.lang.String"/>
	<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
	<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" id="currentOrientation" type="java.lang.String"/>
	<bean:define id="pageId" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" scope="request" type="java.lang.String"/>



    <form name="planForm" action="<html:rewrite page="/mandatoryPlans.do"/>" method="post" id="planForm">
	  <input type="hidden" name="enabled"/>
	  <input type="hidden" name="disabled"/>
	  <input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="confirm"/>

	  <input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
   	  <input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
   	  <input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>
   	  <!-- Filter -->
	  <input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" value=""/>
	  <input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value="<%=filterValue%>"/>
    </form>

	<script language="javascript">
		function submitForm() {

			var formElements = document.forms['selectedPlans'].elements;
			for (i=0; i < formElements.length; i++) {
				if (formElements[i].name == 'selectedPlan') {
					if (formElements[i].checked) {
						document.forms['planForm'].enabled.value = document.forms['planForm'].enabled.value + ',' + formElements[i].value;
					} else {
						document.forms['planForm'].disabled.value = document.forms['planForm'].disabled.value + ',' + formElements[i].value;
					}
				}
			}

			document.forms['planForm'].submit();
		}
	</script>

 <td widht="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
          	<td height="15">
          		<table class="table" width="100%" height="100%" cellpadding="0" cellspacing="0">
              	<tr>
                	<td height="100%" class="table-title"><bean:message key="text.listPlansTableTitle" bundle="plans"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>


		<script language="Javascript">
			function sortBy(field, orientation) {
				document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>'].value=field;
				document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>'].value=orientation;
				document.forms['sortingForm'].submit();
			}
		</script>

	    <form name="sortingForm" action="<html:rewrite page="/mandatoryPlans.do"/>" method="post" id="sortingForm">
		  <input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="list"/>
		  <input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>"/>
		  <input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>"/>
	    </form>



		<tr  valign="top">
			<td width="100%">

				<table width="100%" align="center"  class="table" cellpadding="3" cellspacing="0">

			  	<tr height="20" class="table-column-title">
		          <td align="middle" width="20%"><div align="center"><bean:message key="text.planCode" bundle="plans"/></div></td>
		          <td align="middle" width="40%"><div align="center"><bean:message key="text.description" bundle="plans"/></div></td>
		          <td align="middle" width="20%"><div align="center"><bean:message key="text.state" bundle="plans"/></div></td>
		          <td align="middle" width="20%"><div align="center"><bean:message key="text.mandatory" bundle="plans"/></div></td>
               </tr>

			   <TR class="text8" height="40">
					<TD colspan="8" align="right"><bean:message key="text.descriptionFilter" bundle="plans"/>:
						<INPUT type="text" class="input-field" id=descriptionFilter maxlength="32" size="15" value="<%=filterValue%>"/>
						<SCRIPT language="Javascript">
							function filter() {
								var filterCriteria  = 'description';
								var descriptionFilter = document.getElementById('descriptionFilter').value;
								document.forms['planForm'].elements['<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>'].value=filterCriteria;
								document.forms['planForm'].elements['<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>'].value=descriptionFilter;
								document.forms['planForm'].submit();
							}
						</SCRIPT>
						&nbsp;&nbsp;<A href="javascript:filter()">::<bean:message key="text.filter" bundle="plans"/>::</A>&nbsp;&nbsp;&nbsp;&nbsp;
					</TD>
				</TR>

			  	<tr height="20" class="text8">
			    	<td>
			    		<div align="center">
			    		<auster:sortUrl orderKey="shortDescription" currentOrderKey="<%=currentOrderBy%>"
			    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
			    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
			    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
						    <html:img page="/images/ico_sort.jpg" border="0"/>
				    	</auster:sortUrl>
			    		</div>
			    	</td>
			    	<td>
			    		<div align="center">
						<auster:sortUrl orderKey="description" currentOrderKey="<%=currentOrderBy%>"
			    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
			    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
			    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
						    <html:img page="/images/ico_sort.jpg" border="0"/>
			    		</auster:sortUrl>
			    		</div>
			    	</td>
			    	<td>
			    		<div align="center">
						<auster:sortUrl orderKey="state" currentOrderKey="<%=currentOrderBy%>"
			    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
			    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
			    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
						    <html:img page="/images/ico_sort.jpg" border="0"/>
			    		</auster:sortUrl>
			    		</div>
			    	</td>
                    <td></td>
			  	</tr>



  	  <!--                                          -->
  	  <!-- for each carrier found in bck_carrier_dm -->
  	  <!--                                          -->
<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">

	<bean:define id="planList" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>

	<form name="selectedPlans">
	<logic:iterate name="planList" id="planInfo">

				<tr height="10" class="text8">
      				<td><div align="center"><bean:write name="planInfo" property="shortDescription"/></div></td>
      				<td><div align="center"><bean:write name="planInfo" property="description"/></div></td>
      				<td><div align="center"><bean:write name="planInfo" property="state"/></div></td>
      				<td><div align="center">
						<input type="checkbox" name="selectedPlan" value="<bean:write name="planInfo" property="uid" format="############"/>"
						<logic:equal name="planInfo" property="mandatory" value="true">
						   checked
						</logic:equal>
						/>
						<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
						<script language="Javascript">
							document.forms['planForm'].planInfo.disabled=true;
						</script>
						<% } %>
					</div></td>
                </tr>
	</logic:iterate>
	</form>

				<tr height="45">
			    	<td colspan="4">&nbsp;</td>
			    </tr>
				<tr>
					<td colspan="4" valign="bottom"><div align="center">

	<bean:define id="totalPages" name="<%=RequestScopeConstants.REQUEST_TOTALPAGES_KEY%>" scope="request" type="java.lang.String"/>

	<auster:index pageId="<%=pageId%>" totalPages="<%=totalPages%>"
				  style="text8b"
	              firstUrl="javascript:move({0})"
	              previousUrl="javascript:move({0})"
	              pageIndexUrl="javascript:move({0})"
	              nextUrl="javascript:move({0})"
	              lastUrl="javascript:move({0})"/>

	<script language="Javascript">

		function move(toPage) {
			document.forms['moveToPage'].elements['<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>'].value = toPage;
			var filterCriteria  = 'description';
			var descriptionFilter = document.getElementById('descriptionFilter').value;
			document.forms['moveToPage'].elements['<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>'].value=filterCriteria;
			document.forms['moveToPage'].elements['<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>'].value=descriptionFilter;
			document.forms['moveToPage'].submit();
		}

	</script>

   	<form name="moveToPage" action="<html:rewrite page="/mandatoryPlans.do"/>" method="post">
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="list"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>
		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" value=""/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value="<%=filterValue%>"/>
    </form>

					</div></td>
				</tr>
</logic:notEmpty>

<logic:empty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">
				<tr height="45" class="text8b">
			    	<td colspan="4" class="text8"><div align="center"><bean:message key="text.noPlansFound" bundle="plans"/></div></td>
			    </tr>
</logic:empty>

				<tr>
			    	<td class="text8" height="30" colspan="4" align="right">
			    		<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
			    		<a href="javascript:submitForm()">:: <bean:message key="text.confirm" bundle="plans"/> ::</a>&nbsp;&nbsp;&nbsp;&nbsp;
			    		<% } %>
			    	</td>
			    </tr>

				</table>
			</td>
   		</tr>
   		</table>
   	</td>

