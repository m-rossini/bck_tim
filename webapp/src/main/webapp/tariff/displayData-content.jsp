<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>

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
	function submitForm(option){
		document.searchTariffForm.operation.value=option;
		document.searchTariffForm.submit();
		return;
	}
	function openWindow(tariffZoneUid, serviceUid, packageUid) {
		window.open("<html:rewrite page="/tariffsPopup.do"/>?operation=listHistData" +
					"&rowSelected=" + tariffZoneUid + '-' + serviceUid + '-' + packageUid,
					"tariffsPopup", "scrollbars,resizable,width=650,height=400");
	}
</script>
	
  <% request.setAttribute("resultList", request.getAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY)); %>
  
  <bean:define id="user" name="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>" scope="session" type="br.com.auster.security.model.User"/>
  
  <html:form action="/searchTariff" method="post">
  <td height="100%">
	
		<input type="hidden" name="operation" value="list"/>
		
		<bean:define id="selectedId"  name="<%=TariffPagesConstants.SELECTED_RATE_PLAN%>" scope="request"/>		
		<input type="hidden" name="<%=TariffPagesConstants.SELECTED_RATE_PLAN%>" value="<bean:write name="selectedId"/>"/>
		
		<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
          	<td colspan="2" height="15">
          		<table class="table" width="100%" height="100%" cellpadding="0" cellspacing="0">
	              	<tr>
	                	<td colspan="4" height="100%" class="table-title" align="right"><bean:message key="text.searchTariffTableTitle" bundle="tariff"/></td>
	              	</tr>
              	</table>
            </td>
       	</tr>
       	
       	<tr valign="top">
 			<td colspan="2" height="2"></td>
 		</tr>

		<tr>
			<td width="100%" height="100%">
			<table class="table" align="center" cellpadding="0" cellspacing="0" width="100%" height="100%">
		  	<tr valign="top" class="table-column-title">
		    	<td height="20" width="50%">
		    		<div align="left"><bean:message key="text.selectRateplan" bundle="tariff"/>: <bean:write name="<%=TariffPagesConstants.SELECTED_PLAN_NAME%>"/>
		    		</div>	    		
		    	</td>
		    	<td height="20" width="50%">
		    		<div align="left"><bean:message key="text.selectUf" bundle="tariff"/>: <bean:write name="<%=TariffPagesConstants.SELECTED_PLAN_UF%>"/>
		    		</div>
		    	</td>
	   		</tr>
 	       	<tr valign="top">
	 			<td colspan="2" height="10"></td>
	 		</tr>
	   		<tr>
		    	<td class="text8" height="30" colspan="2" align="right">
		    	<table width="100%" align="center" class="table" cellpadding="3" cellspacing="0">
				  	<tr height="20" class="table-column-title">
				    	<td width="12%">
				    		<a href="javascript:submitForm('listVoice')">
					    		<bean:message key="text.voiceUsageTab" bundle="tariff"/>
					    	</a>
				    	</td>
				    	<td width="12%" class="table-title">
				    		<a href="javascript:submitForm('listData')" style="color:#FFF;">
				    			<bean:message key="text.dataUsageTab" bundle="tariff"/>
				    		</a>
				    	</td>
				    	<td width="12%">
			    			<a href="javascript:submitForm('listService')">
			    				<bean:message key="text.serviceTab" bundle="tariff"/>
			    			</a>
				    	</td>
				    	 
				    	<td width="12%">
			    			<a href="javascript:submitForm('listPackage')">
			    				<bean:message key="text.packageTab" bundle="tariff"/>
			    			</a>
				    	</td>
				    	<td width="12%">
			    			<a href="javascript:submitForm('listMC')">
			    				<bean:message key="text.mcTab" bundle="tariff"/>
			    			</a>
				    	</td>
				    	<td width="12%">
			    			<a href="javascript:submitForm('listNPack')">
			    				<bean:message key="text.npackTab" bundle="tariff"/>
			    			</a>
				    	</td>
				    	<logic:present name="<%=TariffPagesConstants.PLAN_IS_MEUSONHO%>" scope="session">
					    	<td width="12%">
					    		<div align="left">
						    		<a href="javascript:submitForm('listMeuSonho')">
						    			<bean:message key="text.meuSonhoTab" bundle="tariff"/>
						    		</a>
					    		</div>
					    	</td>
				    	</logic:present>
	                </tr>
	            </table>
		    	</td>	    	
		    </tr>
   			<tr>
		    	<td colspan="2" valign="top">
		   		<div id="search-results" class="text8b" style="overflow:auto;width:600px">
		   			<logic:empty name="resultList">
		   				<bean:message key="text.norow" bundle="tariff"/>
		   			</logic:empty>
		   			<logic:notEmpty name="resultList">
					   	<display:table id="listTable" name="resultList" class="text8" cellpadding="0" cellspacing="2">
						  <display:column headerClass="table-column-title" title="Zona tarifária" maxLength="45">
							  	<a href="javascript:openWindow('<c:out value="${listTable.tariffZoneUid}"/>',
							  								   '<c:out value="${listTable.serviceUid}"/>',
							  								   '<c:out value="${listTable.packageUid}"/>')">
							  		<c:out value="${listTable.tariffZone}"/>
							  	</a>
						  </display:column>
						  <display:column headerClass="table-column-title" property="service" title="Serviço" maxLength="15"/>
						  <display:column headerClass="table-column-title" property="effectiveDate" title="Data de efetivação" format="{0,date,dd/MM/yyyy}"/>
						  <display:column headerClass="table-column-title" property="umcode" title="Unidade"/>
						  <display:column headerClass="table-column-title" property="packageName" title="Pacote" maxLength="15"/>
		
						  <display:column headerClass="table-column-title" property="range1" title="Faixa 1"/>
						  <display:column headerClass="table-column-title" property="range1Amount" title="Valor 1" format="R$ {0,number,#,##0.00##########}"/>
						  <display:column headerClass="table-column-title" property="range2" title="Faixa 2"/>
						  <display:column headerClass="table-column-title" property="range2Amount" title="Valor 2" format="R$ {0,number,#,##0.00##########}"/>
						  <display:column headerClass="table-column-title" property="range3" title="Faixa 3"/>
						  <display:column headerClass="table-column-title" property="range3Amount" title="Valor 3" format="R$ {0,number,#,##0.00##########}"/>
						  <display:column headerClass="table-column-title" property="range4" title="Faixa 4"/>
						  <display:column headerClass="table-column-title" property="range5Amount" title="Valor 4" format="R$ {0,number,#,##0.00##########}"/>
						  <display:column headerClass="table-column-title" property="range5" title="Faixa 5"/>
						  <display:column headerClass="table-column-title" property="range5Amount" title="Valor 5" format="R$ {0,number,#,##0.00##########}"/>
						  <display:column headerClass="table-column-title" property="afterRange5Amount" title="Valor Máximo" format="R$ {0,number,#,##0.00##########}"/>
						  <display:column headerClass="table-column-title" property="loadedDate" title="Data de carga" format="{0,date,dd/MM/yyyy}"/>
						</display:table>
					</logic:notEmpty>
				</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			</table>
			</td>
		</tr>
	</table>
  </td>
  </html:form>
  