<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<html:html locale="true">


<head>
  <title>:::: <bean:message key="text.title" bundle="general"/> ::::</title>
  <link href="<html:rewrite page="/css/data_general.css"/>"      rel="stylesheet" type="text/css">
  <link href="<html:rewrite page="/css/data.css"/>"              rel="stylesheet" type="text/css">
  <link href="<html:rewrite page="/jpivot/table/mdxtable.css"/>" rel="stylesheet" type="text/css" >
  <link href="<html:rewrite page="/jpivot/navi/mdxnavi.css"/>"   rel="stylesheet" type="text/css">
  <link href="<html:rewrite page="/wcf/form/xform.css"/>"        rel="stylesheet" type="text/css">
  <link href="<html:rewrite page="/wcf/table/xtable.css"/>"      rel="stylesheet" type="text/css">
  <link href="<html:rewrite page="/wcf/tree/xtree.css"/>"        rel="stylesheet" type="text/css">
</head>



<body>
<table width="100%" height="100%" cellspacing="2" cellpadding="0">
	<tr>
        <td width="10%" class="side-firstLine-background" height="5"></td>
        <td width="189" class="center-firstLine-background"></td>
        <td class="center-firstLine-background"></td>
        <td width="10%" class="side-firstLine-background" height="5"></td>
	</tr>
	<tr  valign="middle">
	    <td class="side-background"></td>
	    <td height="58"><div align="center"><html:img page="/images/logo.jpg" alt="lOGO"/></div></td>
	
	        <tiles:insert attribute="header"/>
	
	    <td class="side-background"><div align="right"></div></td>
	</tr>
	<tr valign="middle" >
	        <td height="5" class="sidebar-background"></td>
	    <td height="5" colspan="2" class="centerbar-background"></td>
	    <td class="sidebar-background"></td>
	</tr>
	<tr valign="top">
	        <td  class="side-background" rowspan="2"  background="<html:rewrite page="/images/bg_dot-fade.gif"/>"></td>
	        <tiles:insert attribute="menu"/>
	        <tiles:insert attribute="body-content"/>
	    <td  rowspan="2" class="side-background" background="<html:rewrite page="/images/bg_dot-fade.gif"/>"></td>
	</tr>
	<tr valign="top">
	    <td colspan="2">
	    	<div align="center"><font class="text7"><bean:message key="text.footerDisclaimer" bundle="general"/>&nbsp;
	        <a href="https://extranet.auster.com.br/vivo/billcheckout" target="_new">
	        	<bean:message key="text.footerDisclaimer2" bundle="general"/></a>
	   		<br>Copyright&copy; 2004-2005 <a href="http://www.auster.com.br" target="_new">Auster Solutions do Brasil</a>
	   		</font></div>
	   </td>
	</tr>
	<tr>
	    <td class="sidebar-background"></td>
	    <td height="5" colspan="2" height="5" class="centerbar-background"></td>
	    <td class="sidebar-background"></td>
	</tr>
</table>
 
</body>
</html:html>
