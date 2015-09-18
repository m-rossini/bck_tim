<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<html>

<body onload="javascript:closeWindow()">

<script language="Javascript">
 function closeWindow() {
  	window.opener.location="discountTable.do?discountUid=<bean:write name="discountUid"/>&discountDescUid=<bean:write name="discountDescUid"/>";
  	window.close();  
 }

</script>

<bean:define id="discountUid" name="discountUid" scope="request"/>
<bean:define id="discountDescUid" name="discountDescUid" scope="request"/>

</body>

</html>
