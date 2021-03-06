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
<%@ page import="br.com.auster.tim.billcheckout.model.Plan"%>


<html>

<head>
	<title>:::: <bean:message key="text.title" bundle="general"/> ::::</title>

	<script language="JAVASCRIPT" src="<html:rewrite page="/js/format.js"/>"></script>
	<script language="JAVASCRIPT" src="<html:rewrite page="/js/calendar.js"/>"></script>

	<link href="<html:rewrite page="/css/data_login.css"/>" rel="stylesheet" type="text/css">
	<link href="<html:rewrite page="/css/data_general.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<html:rewrite page="/css/data.css"/>" rel="stylesheet" type="text/css"/>
</head>



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


<body>

<script language="Javascript">
 function closeWindow() {
 	window.close();
 	return;
 }

  function submitForm() {
  	document.forms['plansDiscountForm'].submit();
  	return;
  }
</script>

<bean:define name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" id="filterValue" type="java.lang.String"/>
<bean:define id="pageId" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" scope="request" type="java.lang.String"/>
<bean:define id="currentPackage" name="<%=RequestScopeConstants.REQUEST_REQID_KEY %>" type="br.com.auster.tim.billcheckout.model.PromotionPackage"/>

<bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session"  type="br.com.auster.security.model.User"/>

  <form name="plansDiscountForm" action="<html:rewrite page="/plansDiscountPopup.do"/>" method="post" id="plansDiscountForm">

	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="confirm"/>
	<input type="hidden" name="uid" value="<%=String.valueOf(currentPackage.getUid())%>"/>
	<!-- Filter -->
	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" value=""/>
	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value="<%=filterValue%>"/>

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
          	<td colspan="4" height="15" class="table-title">
          		<bean:message key="text.listPlansTableTitle" bundle="packages"/>
          	</td>
		</tr>

		<tr colspan="4" valign="top">
        	<td height="5"></td>
        </tr>

	   <tr class="text8" height="40">
			<td colspan="8" align="right"><bean:message key="text.descriptionFilter" bundle="plans"/>:
				<input type="text" class="input-field" id=descriptionFilter maxlength="32" size="15" value="<%=filterValue%>" onkeydown="javascript:onKeyDown()"/>
				<script language="Javascript">
					function onKeyDown() {
						if (window.event.keyCode == 13)
						{
						    event.returnValue=false;
						    event.cancel = true;
						}
					}

					function filter() {
						var filterCriteria  = 'description';
						var descriptionFilter = document.getElementById('descriptionFilter').value;
						document.forms['moveToPage'].elements['<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>'].value=filterCriteria;
						document.forms['moveToPage'].elements['<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>'].value=descriptionFilter;
						document.forms['moveToPage'].submit();
					}
				</script>
				&nbsp;&nbsp;<a href="javascript:filter()">::<bean:message key="text.filter" bundle="plans"/>::</a>&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
		</tr>

<logic:notEmpty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">

	<bean:define id="planList" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>

	<% int counter = 0; %>
	<logic:iterate name="planList" id="planInfo">

		<% if ((counter++ % 2) == 0) { %>
		<tr height="10" class="text8">
      		<td width="15%"><input type="checkbox" name="planUid" value="<bean:write name="planInfo" property="uid" format="########"/>" <% if (currentPackage.containsKey(((Plan)planInfo).getUid())) { out.print(" checked "); } %>/></td>
			<td width="35%" align="left"><bean:write name="planInfo" property="planName"/> (<bean:write name="planInfo" property="state"/>)</td>
		<% } else { %>
      		<td width="15%"><input type="checkbox" name="planUid" value="<bean:write name="planInfo" property="uid" format="########"/>" <% if (currentPackage.containsKey(((Plan)planInfo).getUid())) { out.print(" checked "); } %>/></td>
			<td width="35%" align="left"><bean:write name="planInfo" property="planName"/> (<bean:write name="planInfo" property="state"/>)</td>
		</tr>
		<% } %>
		<input type="hidden" name="displayedPlanUid" value="<bean:write name="planInfo" property="uid" format="########"/>"/>
	</logic:iterate>
		<% if ((counter % 2) == 1) { %>
      		<td colspan="2"/>&nbsp;</td>
		</tr>
		<% } %>

</logic:notEmpty>



<logic:empty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">
		<tr height="10" class="text8b">
	    	<td colspan="4" class="text8"><div align="center">
	    		<bean:message key="text.noPlansFound" bundle="packages"/>
	    	</div></td>
	    </tr>
</logic:empty>

	</form>

		<tr height="25">
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

	   	<form name="moveToPage" action="<html:rewrite page="/plansDiscountPopup.do"/>" method="post">
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="list"/>
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_PAGEID_KEY%>" value="<%=pageId%>"/>
	   		<input type="hidden" name="<%=RequestScopeConstants.REQUEST_MOVETO_KEY%>" value="0"/>
			<input type="hidden" name="uid" value="<%=String.valueOf(currentPackage.getUid())%>"/>
			<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERBY_KEY%>" value=""/>
   			<input type="hidden" name="<%=RequestScopeConstants.REQUEST_FILTERCONDITION_KEY%>" value="<%=filterValue%>"/>
	    </form>

			</div></td>
		</tr>

		<tr>
	    	<td class="text8" height="30" colspan="4" align="right">
	    		<% if (user.getAllowedDomains().contains(PermissionConstants.PERMISSION_ADVANCED_RULECFG_EDIT)) { %>
	    		<a href="javascript:submitForm()">:: <bean:message key="text.confirm" bundle="packages"/> :: </a>&nbsp;&nbsp;
	    		<% } %>
	    		<a href="javascript:closeWindow()">:: <bean:message key="text.cancel" bundle="packages"/> :: </a>&nbsp;&nbsp;&nbsp;&nbsp;
	    	</td>
	    </tr>

	</table>

</body>
</html>
