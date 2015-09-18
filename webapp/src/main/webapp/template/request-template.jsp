<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<html:html locale="true">


<head>

<title>:::: <bean:message key="text.title" bundle="general"/> ::::</title>

<script language="JAVASCRIPT" src="<html:rewrite page="/js/format.js"/>"></script>
<script language="JAVASCRIPT" src="<html:rewrite page="/js/calendar.js"/>"></script>
<script language="JAVASCRIPT" src="<html:rewrite page="/js/dtree.js"/>"></script>
<script language="JAVASCRIPT" src="<html:rewrite page="/js/subModal.js"/>"></script>

<link href="<html:rewrite page="/css/subModal.css"/>" rel="stylesheet" type="text/css" />
<link href="<html:rewrite page="/css/dtree.css"/>" rel="stylesheet" type="text/css"/>
<link href="<html:rewrite page="/css/data_login.css"/>" rel="stylesheet" type="text/css"/>
<link href="<html:rewrite page="/css/data_general.css"/>" rel="stylesheet" type="text/css"/>
<link href="<html:rewrite page="/css/data.css"/>" rel="stylesheet" type="text/css"/>
<link href="<html:rewrite page="/css/displaytag-custom.css"/>" rel="stylesheet" type="text/css"/>
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
	    <td class="side-background"/>
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
          <div align="center"><font class="text7">
            <br/>Copyright&copy; 2004-2005 <a href="http://www.auster.com.br" target="_new">Auster Solutions do Brasil</a>
            </font></div>
	   </td>
	</tr>
	<tr>
	    <td class="sidebar-background"></td>
	    <td height="5" colspan="2" class="centerbar-background"></td>
	    <td class="sidebar-background"></td>
	</tr>
</table>

</body>
</html:html>
