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

	<script language="javascript">
		function submitForm() {
			if(trim(document.searchForm.elements['parameterValue'].value) == '') {
				if(document.searchForm.elements['parameterName'].value == 'custCode') {
					alert("Digite um Código de Cliente");
				} else {
					alert("Digite um Número de Contrato");
				} 
			}	
			else {
				document.searchForm.submit();	
			}			
		}
		
		function trim(str){
			return str.replace(/^\s+|\s+$/g,"");
		}
	</script>


 <html:form action="/searchParameters" method="post" id="searchForm">
 <html:hidden name="searchType" value ="NAVIGATION" property="searchType"/>
 <td widht="100%">   	   	   	
   	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <th scope="col"><table width="100%" class="table" height="15" align="center" cellpadding="0" cellspacing="0" >
                <tr>
                  <td class="menu-title"><bean:message key="title.clientContractScreenTitle" bundle="bscsintegration"/> 
                    
                    &nbsp; </td>
                </tr>
            </table></th>
          </tr>
          <tr>
            <td height="2"></td>
          </tr>
          <tr>
            <td height="100%" valign="top"><table width="100%" height="100%" border="0" cellpadding="20" cellspacing="0" class="table">
                <tr valign="top">
                  <td><p align="center" class="text8">&nbsp; </p>
                    <p align="center" class="text8"><b><bean:message key="text.searchFor" bundle="bscsintegration"/> </b>&nbsp;
                          <html:select property="parameterName"  styleClass="input-field" tabindex="1">                          
	                          	<logic:notEmpty name="configured.parameters" scope="request">	
									<bean:define id="searchParameters" name="configured.parameters" scope="request"/>
									<html:optionsCollection name="searchParameters" value="value" label="key"/>					      															      				
								</logic:notEmpty>                          
                          </html:select>
                    &nbsp;
                    <html:text property="parameterValue" size="20" maxlength="64" tabindex="2"/>
                    <A href="javascript:submitForm()">OK</A></p>
                  <p class="text8">&nbsp;</p>                  </td>
                </tr>
            </table></td>
          </tr>
        </table>
</TD>

</html:form>