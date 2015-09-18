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
   <tiles:insert attribute="body-content"/>
</body>
</html:html>
