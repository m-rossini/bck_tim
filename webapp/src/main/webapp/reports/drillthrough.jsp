<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/reports/drillthrough-template.jsp" flush="true">
  <tiles:put name="body-content" value="/reports/drillthrough-content.jsp" /> 
</tiles:insert>