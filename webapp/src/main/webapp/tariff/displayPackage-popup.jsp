<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>

<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.tim.billcheckout.portal.tariff.TariffPagesConstants"%>

<head>

<title>:::: <bean:message key="text.packageTab" bundle="tariff"/> ::::</title>

<link href="<html:rewrite page="/css/data.css"/>" rel="stylesheet" type="text/css"/>
<link href="<html:rewrite page="/css/data_general.css"/>" rel="stylesheet" type="text/css"/>
<link href="/billcheckout/css/displaytag-custom.css" rel="stylesheet" type="text/css"/>

</head>

<% request.setAttribute("resultList", request.getAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY)); %>

	<table class="table" align="center" cellpadding="3" cellspacing="3" width="100%" height="100%" border="2">
	  	<tr valign="top" class="table-column-title">
	    	<td height="20" width="50%">
	    		<div align="left">
	    			<bean:message key="text.selectRateplan" bundle="tariff"/>: <bean:write name="<%=TariffPagesConstants.SELECTED_PLAN_NAME%>"/>
		    	</div>	    		
		    </td>
		    <td height="20" width="50%">
		    	<div align="left">
		    		<bean:message key="text.selectUf" bundle="tariff"/>: <bean:write name="<%=TariffPagesConstants.SELECTED_PLAN_UF%>"/>
		    	</div>
		    </td>
	   	</tr>
		<tr>
			<td width="100%" colspan="2" valign="top">
				<div id="search-results" class="text8b">
					<logic:empty name="resultList">
						<bean:message key="text.norow" bundle="tariff"/>
					</logic:empty>
				</div>
				<logic:notEmpty name="resultList">
				   	<display:table class="text8" name="resultList" cellpadding="2" cellspacing="2">
				   		  <display:column headerClass="table-column-title" property="description" 	title="Pacote"/>
						  <display:column headerClass="table-column-title" property="planDescr"     title="Plano" />
						  <display:column headerClass="table-column-title" property="effectiveDate" title="Efetivação"  format="{0,date,dd/MM/yyyy}"/>
						  <display:column headerClass="table-column-title" property="accessFee"     title="Mensalidade" format="R$ {0,number,#,##0.00##}"/>
						  <display:column headerClass="table-column-title" property="subscrRate"    title="Ativação"    format="R$ {0,number,#,##0.00##}"/>
						  <display:column headerClass="table-column-title" property="loadedDate"    title="Carga" 	    format="{0,date,dd/MM/yyyy}"/>
					</display:table>
				</logic:notEmpty>
			</td>
		</tr>
	</table>