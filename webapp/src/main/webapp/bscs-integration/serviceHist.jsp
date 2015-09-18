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


<head>

<script language="JAVASCRIPT" src="<html:rewrite page="/js/format.js"/>"></script>
<script language="JAVASCRIPT" src="<html:rewrite page="/js/calendar.js"/>"></script>
<script language="JAVASCRIPT" src="<html:rewrite page="/js/dtree.js"/>"></script>
<script language="JAVASCRIPT" src="<html:rewrite page="/js/subModal.js"/>"></script>

<link href="<html:rewrite page="/css/subModal.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/css/dtree.css"/>" rel="stylesheet" type="text/css"/>
<link href="<html:rewrite page="/css/data_login.css"/>" rel="stylesheet" type="text/css"/>
<link href="<html:rewrite page="/css/data_general.css"/>" rel="stylesheet" type="text/css"/>
<link href="<html:rewrite page="/css/data.css"/>" rel="stylesheet" type="text/css"/>
<link href="<html:rewrite page="/css/subModal.css"/>" rel="stylesheet" type="text/css" />

</head>

<html lang="pt">



<table width="400px" height="200px" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <th align="left" class="text8" scope="col"><bean:message key="text.serviceHistoryTableTitle" bundle="bscsintegration"/> <bean:write name="<%=RequestScopeConstants.REQUEST_REQINFO_KEY%>" scope="request"/></th>
  </tr>
  <tr>
    <td><table width="100%" height="100%" class="table">
      <tr class="table-column-title">
        <th width="30%" align="center" bgcolor="#EFEFEF" scope="col"><bean:message key="text.serviceHistoryDate" bundle="bscsintegration"/></th>
        <th width="70%" align="center" bgcolor="#EFEFEF" scope="col"><bean:message key="text.serviceHistoryStatus" bundle="bscsintegration"/></th>
      </tr>
	  <logic:notEmpty name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request">	
	  	<bean:define id="serviceHistMap" name="<%=RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY%>" scope="request"/>							
		<logic:iterate id="serviceHist" name="serviceHistMap">
			<tr>
		    	<td align="center" class="text8"><bean:write name="serviceHist" property="key"/></td>
		    	<td align="center" class="text8"><bean:write name="serviceHist" property="value"/></td>
		    </tr>									      				
		</logic:iterate>
	  </logic:notEmpty>              
    </table></td>
  </tr>
</table>


</html>