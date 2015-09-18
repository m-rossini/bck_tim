<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<%@ page import="br.com.auster.dware.console.commons.RequestScopeConstants"%>
<%@ page import="br.com.auster.tim.billcheckout.portal.tariff.TariffPagesConstants"%>

<head>
	<title>:::: <bean:message key="text.npackTab" bundle="tariff"/> ::::</title>
	<link href="<html:rewrite page="/css/data.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<html:rewrite page="/css/data_general.css"/>" rel="stylesheet" type="text/css"/>
	<link href="<html:rewrite page="/css/displaytag-custom.css"/>" rel="stylesheet" type="text/css"/>
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
				   	<display:table class="text8" name="resultList" cellpadding="2" cellspacing="2" id="row">
					  	  <display:column headerClass="table-column-title" title="Zona Tarifária">
					  	  	<c:out value="${row}"/>
					  	  </display:column>
				    </display:table>
				</logic:notEmpty>
			</td>
		</tr>
	</table>
