<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp"%>
<%@ taglib uri="http://www.tonbeller.com/wcf" prefix="wcf"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>

<html>
<body onLoad="checkIfClosed('onLoad')" onUnload="checkIfClosed('onUnload')">
      
<form action="<html:rewrite page="/reports/drillthrough.jsp"/>" method="get" onSubmit="markClosedFromForm(this)">
	<!-- drill through table -->
	<wcf:render ref="query01.drillthroughtable" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>
	<input type="hidden" name="submitingForm" value="false"/>
</form>





<script>

function markClosedFromForm(formObj) {
   formObj.submitingForm.value='true';
   return true;
}

function checkIfClosed(actionName) {
 	//window.alert('on action=' + actionName + ' my url is = ' + document.URL);
 	closeIt = 'false';
 	if ((actionName == 'onLoad') &&	
 	    (document.URL.indexOf('query01.drillthroughtable.close') > 0)) {
 		closeIt = 'true';
 	} 	
 	if ((actionName == 'onUnload') &&	
 	    (document.forms[0].submitingForm.value == 'false')) {
 	  closeIt = 'true';
 	}
 	
  if (closeIt == 'true') {
	    submitParent();
	    window.close();
	}
}

function submitParent() {
	var doc = window.opener.document;
	var formAttrs = doc.getElementById('closingAttrs');
	var closingObj = doc.createElement('input');
	closingObj.setAttribute('type', 'hidden');
	closingObj.setAttribute('name', 'query01.drillthroughtable.close');
  closingObj.setAttribute('value', 'true');
	formAttrs.appendChild(closingObj);
	doc.forms['queryform'].submit();
}

</script>

</body>
</html>