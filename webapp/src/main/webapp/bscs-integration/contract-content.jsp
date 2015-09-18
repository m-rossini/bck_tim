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

<!--                          -->
<!-- right-side content table -->
<!--                          -->
<script language="Javascript">
	
	function submitAccountForm(account) {								
		var parameterName = document.searchForm.elements['parameterName'];
		
		for (var i = 0; i < parameterName.options.length; i++) {
			if (parameterName.options[i].text == 'Cliente') {
				parameterName.selectedIndex=i;
			}
		}
		
		document.searchForm.elements['parameterValue'].value=account;
		
		document.searchForm.submit();
	}
	
	function submitServicesHistForm(contract) {				
		var parameterName = document.searchForm.elements['parameterName'];
		
		for (var i = 0; i < parameterName.options.length; i++) {
			if (parameterName.options[i].text == 'Contrato') {
				parameterName.selectedIndex=i;
			}
		}
		
		document.searchForm.elements['parameterValue'].value=contract;
		document.searchForm.elements['searchType'].value="NAVIGATION_SERV_HIST";
		
		document.searchForm.submit();
	}
	
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
	
   function filter (phrase, _id, cellNr){
		 var suche = phrase.value.toLowerCase();
		 var table = document.getElementById(_id);
		 var ele;
		 for (var r = 3; r < table.rows.length; r++){
		     ele = table.rows[r].cells[cellNr].innerHTML.replace(/<[^>]+>/g,"");
		     if (ele.toLowerCase().indexOf(suche)>=0 )
		   table.rows[r].style.display = '';
		     else table.rows[r].style.display = 'none';
		 }
    }	
</script>

	<td height="100%">
		
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
                  <td width="200"><div class="dtree">                  	  
                     <script type="text/javascript">
						var nodeArray = new Array;
						var superNodeArray = new Array;
						var index = -1;													
						d = new dTree('d');							
				       <logic:notEmpty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" property="hierarchyStructure" scope="request">	
							<bean:define id="hierarchyStructure" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" property="hierarchyStructure" scope="request"/>							
							<logic:iterate id="contract" name="hierarchyStructure"> 
								index++;
								nodeArray[index] = '<bean:write name="contract" property="key"/>';
								var superNode = '<bean:write name="contract" property="value"/>';
								if(superNode == null || superNode == '') {
									superNodeArray[index] = '-1';
								} else {
									superNodeArray[index] = superNode;	
								}																																																																									
				        	</logic:iterate>
						</logic:notEmpty> 																								
						for (var i = 0; i < superNodeArray.length; i++) {
							if (superNodeArray[i]=='-1') {					
								d.add(i, -1, nodeArray[i],'javascript:submitAccountForm(\'' + nodeArray[i] + '\')', '', '_self'); 									
							} else {
								for (var j = 0; j< nodeArray.length; j++) {
									if(nodeArray[j] == superNodeArray[i]) {
										d.add(i, j, nodeArray[i],'javascript:submitAccountForm(\'' + nodeArray[i] + '\')', '', '_self'); 									
									}	
								}
							}
						}							
						d.draw();
	                 </script>
                  </div></td>
                  <html:form action="/searchParameters" method="post" id="searchForm">
					  <html:hidden name="searchType" value ="NAVIGATION" property="searchType"/>
	                  <td><p align="right" class="text8"> <b><bean:message key="text.searchFor" bundle="bscsintegration"/> </b>&nbsp;
                          <html:select property="parameterName"  styleClass="input-field" tabindex="1">                          
	                          	<logic:notEmpty name="configured.parameters" scope="request">	
									<bean:define id="searchParameters" name="configured.parameters" scope="request"/>
									<html:optionsCollection name="searchParameters" value="value" label="key"/>					      															      				
								</logic:notEmpty>                          
                          </html:select>          
	                    &nbsp
	                    <html:text property="parameterValue" size="20" maxlength="64" tabindex="2"/>
	                    <A href="javascript:submitForm()">OK</A></p>
                    </html:form>
					  <bean:define id="contractNumber" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" property="contractNumber" scope="request"/>							                   	                    
                      <p class="text8"><b><bean:message key="text.contract" bundle="bscsintegration"/> <bean:write name="contractNumber"/></b></p>
                    <b class="text8b"><bean:message key="text.generalInfoTableTitle" bundle="bscsintegration"/></b>                     
                      <table width="100%" class="table"> 
                        <bean:define id="generalInfo" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" property="generalInfo" scope="request"/>							                   	
                        <tr>
                          <td align="left" width="35%" class="table-column-title"><bean:message key="text.subscriberClientName" bundle="bscsintegration"/></td>
                          <td align="left" width="65%" class="text8"><bean:write name="generalInfo" property="subscriberName"/></td>
                        </tr>
                        <tr>
                          <td align="left" width="35%" class="table-column-title"><bean:message key="text.contractPlan" bundle="bscsintegration"/></td>
                          <td align="left" width="65%" class="text8"><bean:write name="generalInfo" property="planName"/></td>
                        </tr>
                        <tr>
                          <td align="left" width="35%" class="table-column-title"><bean:message key="text.subscriberClientStatus" bundle="bscsintegration"/></td>
                          <td align="left" width="65%" class="text8"><bean:write name="generalInfo" property="status"/></td>
                        </tr>
                        <tr>
                          <td align="left" width="35%" class="table-column-title"><bean:message key="text.contractDateActivation" bundle="bscsintegration"/> </td>
                          <td align="left" width="65%" class="text8"><bean:write name="generalInfo" property="activactionDate"/></td>
                        </tr>
                        <tr>
                          <td align="left" width="35%" class="table-column-title"><bean:message key="text.accessNumber" bundle="bscsintegration"/>: </td>
                          <td align="left" width="65%" class="text8"><bean:write name="generalInfo" property="acessNumber"/></td>
                        </tr>
                      </table>
                    <b><br />
                      <span class="text8b"><bean:message key="text.packageTableTitle" bundle="bscsintegration"/></span> </b>
                      <table width="100%" class="table">
                        <tr class="table-column-title">
                          <td align="center"><bean:message key="text.packageName" bundle="bscsintegration"/> </td>
                          <td align="center"><bean:message key="text.packageDateActivation" bundle="bscsintegration"/> </td>                       
                        <logic:notEmpty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" property="packageList" scope="request">	
							<bean:define id="packageList" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" property="packageList" scope="request"/>							
							<logic:iterate id="pack" name="packageList">
		                        <tr>
		                          <td align="center" class="text8"><bean:write name="pack" property="name"/></td>
		                          <td align="center" class="text8"><bean:write name="pack" property="activationDate"/></td>
		                        </tr>									      				
                        	</logic:iterate>
						</logic:notEmpty> 
                      </table>                    
                    <br />
                      <b class="text8b"><bean:message key="text.activeServicesTableTitle" bundle="bscsintegration"/> </b><br />
                      <table width="100%" class="table" id="serviceList">
                        <tr class="table-column-title">                          
                          <td align="70%"><bean:message key="text.serviceDescription" bundle="bscsintegration"/></td>
                          <td width="30%" align="center"><bean:message key="text.serviceActivationDate" bundle="bscsintegration"/></td>
                        </tr>
                        <tr>
                          <td align="center" class="text8"><INPUT name="text" type="text" class="input-field" id="text" value="" size="10" maxlength="10" onKeyUp="javascript:filter(this, 'serviceList', '0')"/>
                          <br>
                          <td align="center" class="text8">&nbsp;</td>
                        </tr>
                        <tr>
                          <td align="center" class="text8"><a href="javascript:sortBy('cycleCode','page.order.forward')" ><img src="images/ico_sort.jpg" width="17" height="17" border="0"></a></td>
                          <td align="center" class="text8"><a href="javascript:sortBy('cycleCode','page.order.forward')" ></a></td>
                        </tr>
                        <form name="serviceHistForm" action="<html:rewrite page="/showServiceHist.do"/>" method="post" id="serviceHistForm">
                        	<input type="hidden" name="<%=RequestScopeConstants.REQUEST_REQID_KEY%>"/>
	                        <logic:notEmpty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" property="activeServiceList" scope="request">	
								<bean:define id="activeServiceList" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" property="activeServiceList" scope="request"/>							
								<logic:iterate id="activeService" name="activeServiceList">
			                        <tr>
			                          <td align="center" class="text8">
			                          	<a class="submodal" href="<html:rewrite page="/showServiceHist.do"/>?<%=RequestScopeConstants.REQUEST_REQID_KEY%>=<bean:write name="activeService" property="snCode"/>&<%=RequestScopeConstants.REQUEST_REQINFO_KEY%>=<bean:write name="activeService" property="description"/>">
			                          		<bean:write name="activeService" property="description"/>
			                          	</a>
			                          </td>
			                          <td align="center" class="text8"><bean:write name="activeService" property="serviceActivation"/></td>
			                        </tr>									      				
	                        	</logic:iterate>
							</logic:notEmpty>                        
						</form>
                    </table>
                  <p><span class="text8"><A href="javascript:submitServicesHistForm(<bean:write name="contractNumber"/>)"> :: <bean:message key="text.serviceHistoryLinkText" bundle="bscsintegration"/> :: </A></span></p></td>
                </tr>
            </table></td>
          </tr>
        </table></TD>	