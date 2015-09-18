<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/reports/queries-template.jsp" flush="true">
  <tiles:put name="header" value="/template/header.jsp" />
  <tiles:put name="menu" value="/template/menu.jsp" />
  <tiles:put name="body-content" value="/reports/queries-content.jsp" /> 
</tiles:insert>