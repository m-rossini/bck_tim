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

		if (validateDiscountRangeForm(document.discountRangeForm)) {

			var upper = document.discountRangeForm.upperLimit;
			var lower = document.discountRangeForm.lowerLimit;
			if (validateAmounts(upper, lower, '<bean:message key="errors.upperLowerRange" bundle="progressiveDiscount"/>')) {

				var discountRate = new Number(removeFormat(document.discountRangeForm.discountRate.value));
				if (discountRate > 100) {
					window.alert('<bean:message key="errors.discountRange" bundle="progressiveDiscount"/>');
				}
				// removing all format
				document.discountRangeForm.upperLimit.value = upper.value;
				document.discountRangeForm.lowerLimit.value = lower.value;
				document.discountRangeForm.discountRate.value = discountRate;
				document.discountRangeForm.submit();
			}
		}
	}
</script>

<bean:define id="ongoingOperation" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" scope="request"/>
<bean:define id="discountRange" name="<%=RequestScopeConstants.REQUEST_REQID_KEY %>" type="br.com.auster.tim.billcheckout.model.ProgressiveDiscountRates"/>
<bean:define id="discountUid" name="discountUid" scope="request"/>
<bean:define id="discountDescUid" name="discountDescUid" scope="request"/>

  <html:javascript formName="discountRangeForm"/>

  <html:form  action="/discountRangePopup" method="post" id="discountRangeForm">

	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_OPERATION_DATA%>" value="<%=ongoingOperation%>"/>
	<html:hidden property="uid" value="<%=String.valueOf(discountRange.getUid())%>"/>
	<html:hidden property="discountUid" value=""/>
        <script>
       		document.forms['discountRangeForm'].discountUid.value='<bean:write name="discountUid"/>';
       	</script>
	<html:hidden property="discountDescUid" value=""/>
		<script>
        	document.forms['discountRangeForm'].discountDescUid.value='<bean:write name="discountDescUid"/>';
        </script>

        <!--                          -->
        <!-- right-side content table -->
        <!--                          -->

		<table width="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
        	<logic:equal name="ongoingOperation" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT%>">
	            <td colspan="4" class="table-title" align="right"><bean:message key="text.addDiscountRange" bundle="progressiveDiscount"/></td>
	        </logic:equal>
			<logic:equal name="ongoingOperation" value="<%=RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE%>">
	        	<td colspan="4" class="table-title" align="right"><bean:message key="text.updateDiscountRange" bundle="progressiveDiscount"/></td>
	        </logic:equal>
		</tr>

		<tr colspan="4" valign="top">
        	<td height="5"></td>
        </tr>

	   <tr class="text8" height="40">
			<table width="100%" class="table">
				<tr>
                	<td align="left" width="25%" class="table-column-title"><bean:message key="text.lowerLimit" bundle="progressiveDiscount"/></td>
                    <td align="left" width="25%" class="table-column-title"><bean:message key="text.upperLimit" bundle="progressiveDiscount"/></td>
                    <td align="left" width="25%" class="table-column-title"><bean:message key="text.discountRate" bundle="progressiveDiscount"/></td>
                </tr>
                <tr>
                    <td><div align="center">
                    		<html:text styleClass="mandatory-input-field" property="lowerLimit" size="8" maxlength="16" tabindex="1" value=""/>
                    		<script>
                    			document.forms['discountRangeForm'].lowerLimit.value='<bean:write name="discountRange" property="lowerLimit" format="###,###,##0.00"/>';
								document.forms['discountRangeForm'].lowerLimit.style.textAlign = "right";
								document.forms['discountRangeForm'].lowerLimit.onkeypress = displayFormattedMonetaryAmount;
								document.forms['discountRangeForm'].lowerLimit.onkeydown	= storeKeyPressed;
				          	</script>
                    	</div>
   					</td>
                    <td><div align="center">
                    		<html:text styleClass="input-field" property="upperLimit" size="8" maxlength="16" tabindex="2" value=""/>
                    		<script>
				          		document.forms['discountRangeForm'].upperLimit.value='<bean:write name="discountRange" property="upperLimit" format="###,###,##0.00"/>';
								document.forms['discountRangeForm'].upperLimit.style.textAlign = "right";
								document.forms['discountRangeForm'].upperLimit.onkeypress = displayFormattedMonetaryAmount;
								document.forms['discountRangeForm'].upperLimit.onkeydown	= storeKeyPressed;
				          	</script>
                    	</div>
 					</td>
                    <td><div align="center">
                    	  	<html:text styleClass="mandatory-input-field" property="discountRate" size="8" maxlength="16" tabindex="3" value=""/>
                    		<script>
				          		document.forms['discountRangeForm'].discountRate.value='<bean:write name="discountRange" property="discountRate" format="###,###,##0.00"/>';
								document.forms['discountRangeForm'].discountRate.style.textAlign = "right";
								document.forms['discountRangeForm'].discountRate.onkeypress = displayFormattedMonetaryAmount;
								document.forms['discountRangeForm'].discountRate.onkeydown	= storeKeyPressed;
				          	</script>
                    	</div>
 					</td>
                </tr>
             </table>
		</tr>

		<br/>

		<tr>
	    	<td class="text8" colspan="4">
	    		<font class="text8">
	    			<a href="javascript:submitForm()">:: <bean:message key="text.confirm" bundle="packages"/> :: </a>&nbsp;&nbsp;
	    			<a href="javascript:closeWindow()">:: <bean:message key="text.cancel" bundle="packages"/> :: </a>&nbsp;&nbsp;&nbsp;&nbsp;
	    		</font>
	    	</td>
	    </tr>

	</table>
	</html:form>
</body>
</html>
