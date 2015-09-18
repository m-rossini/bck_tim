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


<script language="Javascript">
	function submitForm(objId, type) {
		document.forms['packageForm'].elements['<%=RequestScopeConstants.REQUEST_REQID_KEY%>'].value = objId;
		document.forms['packageForm'].elements['<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>'].value = type;

		if (type == '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>') {
			if (!confirm('<bean:message key="message.sureToErase" bundle="packages"/> ' + objId + ' ?')) {
				return;
			};
		}
		document.forms['packageForm'].submit();
	}
</script>


  <form name="packageForm" action="<html:rewrite page="/promotionPackages.do"/>" method="post" id="packageForm">
	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_REQID_KEY%>"/>
	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="unspecified"/>
	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>"/>
	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>"/>
  </form>

  <bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"  type="br.com.auster.security.model.User"/>

  <td widht="100%">

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
          	<td height="15">
          		<table class="table" width="100%" height="100%" cellpadding="0" cellspacing="0">
              	<tr>
                	<td height="100%" class="table-title"><bean:message key="text.listPackageTableTitle" bundle="packages"/></td>
              	</tr>
            	</table>
            </td>
		</tr>
		<tr valign="top">
        	<td height="2"></td>
        </tr>

		<form name="sortingForm" method="POST" action="<html:rewrite page="/promotionPackages.do"/>" >
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>"/>
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="list"/>
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>"/>
		</form>

		<script language="Javascript">
			function sortBy(field, orientation) {
				document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>'].value=field;
				document.forms['sortingForm'].elements['<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>'].value=orientation;
				document.forms['sortingForm'].submit();
			}
		</script>

		<tr  valign="top">
			<td  height="100%" width="100%">

				<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" id="currentOrderBy" type="java.lang.String"/>
				<bean:define name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" id="currentOrientation" type="java.lang.String"/>

				<table width="100%" height="100%" align="center"  class="table" cellpadding="3" cellspacing="0">

			  	<tr height="20" class="table-column-title">
			    	<td width="15%"><div align="center"><bean:message key="text.packageCode" bundle="packages"/></div></td>
			    	<td width="70%"><div align="center"><bean:message key="text.packageDescription" bundle="packages"/></div></td>
			    	<td width="15%"><div align="center">&nbsp;&nbsp;<bean:message key="text.remove" bundle="packages"/></div></td>
               </tr>

			  	<tr height="20" class="text8">
			    	<td width="15%">
			    		<div align="center">
			    		<auster:sortUrl orderKey="shortDescription" currentOrderKey="<%=currentOrderBy%>"
			    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
			    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
			    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
						    <html:img page="/images/ico_sort.jpg" border="0"/>
				    	</auster:sortUrl>
			    		</div>
			    	</td>
			    	<td width="70%">
			    		<div align="center">
						<auster:sortUrl orderKey="description" currentOrderKey="<%=currentOrderBy%>"
			    						currentOrientation="<%=currentOrientation%>" url="javascript:sortBy(''{0}'',''{1}'')"
			    					    backwardFlag="<%=RequestScopeConstants.REQUEST_ORDERBACKWARD_KEY%>"
			    					    forwardFlag="<%=RequestScopeConstants.REQUEST_ORDERFORWARD_KEY%>">
						    <html:img page="/images/ico_sort.jpg" border="0"/>
			    		</auster:sortUrl>
			    		</div>
			    	</td>
                    <td width="15%">&nbsp;</td>
			  	</tr>


<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">

	<bean:define id="packageList" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>

	<logic:iterate name="packageList" id="packageInfo">

				<tr height="10" class="text8">
      				<td><div align="center">
      					<a href="javascript:submitForm('<bean:write name="packageInfo" property="uid" format="############"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DETAIL%>')">
      					<bean:write name="packageInfo" property="shortDescription"/>
      					</a></div>
      				</td>
      				<td><div align="center"><bean:write name="packageInfo" property="description"/></div></td>
					<td><div align="center">
						<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
						<a href="javascript:submitForm('<bean:write name="packageInfo" property="uid" format="############"/>', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE%>')">
							<img src="images/no.jpg" border="0"/>
						</a>
						<% } %>
					</td>
                </tr>
	</logic:iterate>

				<tr height="45">
			    	<td colspan="3">&nbsp;</td>
			    </tr>
				<tr>
					<td colspan="6" valign="bottom"><div align="center">

	<bean:define id="pageId" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" scope="request" type="java.lang.String"/>
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
			document.forms['moveToPage'].submit();
		}

	</script>

   	<form name="moveToPage" action="<html:rewrite page="/promotionPackages.do"/>" method="post">
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>
		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="list"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERBY_KEY%>" value="<%=currentOrderBy%>"/>
   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_ORDERWAY_KEY%>" value="<%=currentOrientation%>"/>
    </form>

					</div></td>
				</tr>
</logic:notEmpty>



<logic:empty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">
				<tr height="45" class="text8b">
			    	<td colspan="6" class="text8"><div align="center"><bean:message key="text.noPackagesFound" bundle="packages"/></div></td>
			    </tr>
</logic:empty>

				<tr>
			    	<td class="text8" height="30" colspan="3" align="right">
			    		<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
			    		<a href="javascript:submitForm('', '<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_DETAIL%>')">
							<bean:message key="text.newPackage" bundle="packages"/>
						</a>
						<% } %>
			    	</td>
			    </tr>

				</table>
			</td>
   		</tr>
   		</table>
   	</td>

