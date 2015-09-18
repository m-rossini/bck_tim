<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp"%>
<%@ taglib uri="http://www.tonbeller.com/wcf" prefix="wcf"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/auster-taglib.tld" prefix="auster"%>


<%@ page import="br.com.auster.dware.console.commons.SessionScopeConstants"%>
<%@ page import="br.com.auster.dware.console.error.ExceptionConstants"%>

<auster:checkLogon
   	    sessionKey="<%=SessionScopeConstants.SESSION_USERINFO_KEY%>">
<body>
	<form name="redirectForm" action="<html:rewrite page="/errorPage.do"/>" method="post">
		<input type="hidden" name="<%=ExceptionConstants.USERNOTLOGGED_KEY%>" value="true"/>
	</form>
</body>
<script language="javascript">
	document.forms['redirectForm'].submit();
</script>

</auster:checkLogon>


	<%-- include query and title, so this jsp may be used with different queries --%>
	<wcf:include id="include01" httpParam="query" prefix="/WEB-INF/queries/" suffix=".jsp"/>
	<c:if test="${query01 == null}">
	  <jsp:forward page="/list-requests.do"/>
	</c:if>

	<td height="100%">
		<table width="100%"  height="100%" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<td width="100%" height="15" class="table-title"><bean:message key="text.dynQueriesTableTitle" bundle="general"/> :: <c:out value="${title01}"/></td>
		</tr>
        <tr>
        	<td height="2"></td>
        </tr>
		<tr valign="top">
			<td width="100%">
				<table width="100%" height="100%" class="table" cellpadding="1" cellspacing="0">
				<tr valign="top">
					<td width="100%">


			<logic:present name="query01.drillthroughtable" scope="session">
				<!--
				<logic:equal name="query01.drillthroughtable" scope="session" property="visible" value="true">
	  				<script>window.open('<html:rewrite page="/reports/drillthrough.jsp"/>', 'drillPage', 'menubar=false,location=false,titlebar=false,resizable=true,toolbar=false,status=false,scrollbars=yes')</script>
	  				<bean:define id="drillClosed" scope="session" value=""/>
	  		</logic:equal>
	  		-->
				<logic:notEqual name="query01.drillthroughtable" scope="session" property="visible" value="true">
				    <logic:notEmpty name="drillClosed" scope="session">
	  					<script>window.open('<html:rewrite page="/reports/close.jsp"/>', 'drillPage')</script>
					</logic:notEmpty>
	  				<bean:define id="drillClosed" scope="session" value="true"/>
				</logic:notEqual>
			</logic:present>

			<form name="queryform" action="<html:rewrite page="/reports/displayQuery.jsp"/>" method="post">

				<span id="closingAttrs"/>

				<%-- define table, navigator and forms --%>
				<jp:table id="table01" query="#{query01}"/>
				<jp:navigator id="navi01" query="#{query01}" visible="false"/>
				<wcf:form id="mdxedit01" xmlUri="/WEB-INF/jpivot/table/mdxedit.xml" model="#{query01}" visible="false"/>
				<wcf:form id="sortform01" xmlUri="/WEB-INF/jpivot/table/sortform.xml" model="#{table01}" visible="false"/>

				<jp:print id="print01"/>
				<wcf:form id="printform01" xmlUri="/WEB-INF/jpivot/print/printpropertiesform.xml" model="#{print01}" visible="false"/>

				<jp:chart id="chart01" query="#{query01}" visible="false"/>
				<wcf:form id="chartform01" xmlUri="/WEB-INF/jpivot/chart/chartpropertiesform.xml" model="#{chart01}" visible="false"/>
				<!--wcf:table id="query01.drillthroughtable" visible="false" selmode="none" editable="true"/-->

				<%-- define a toolbar --%>
				<wcf:toolbar id="toolbar01" bundle="com.tonbeller.jpivot.toolbar.resources">
				  <wcf:scriptbutton id="cubeNaviButton" tooltip="toolb.cube" img="cube" model="#{navi01.visible}"/>
				  <!--wcf:scriptbutton id="mdxEditButton" tooltip="toolb.mdx.edit" img="mdx-edit" model="#{mdxedit01.visible}"/-->
				  <wcf:scriptbutton id="sortConfigButton" tooltip="toolb.table.config" img="sort-asc" model="#{sortform01.visible}"/>
				  <wcf:separator/>
				  <!--wcf:scriptbutton id="levelStyle" tooltip="toolb.level.style" img="level-style" model="#{table01.extensions.axisStyle.levelStyle}"/-->
				  <!--wcf:scriptbutton id="hideSpans" tooltip="toolb.hide.spans" img="hide-spans" model="#{table01.extensions.axisStyle.hideSpans}"/-->
				  <!--wcf:scriptbutton id="propertiesButton" tooltip="toolb.properties"  img="properties" model="#{table01.rowAxisBuilder.axisConfig.propertyConfig.showProperties}"/-->
				  <wcf:scriptbutton id="nonEmpty" tooltip="toolb.non.empty" img="non-empty" model="#{table01.extensions.nonEmpty.buttonPressed}"/>
				  <wcf:scriptbutton id="swapAxes" tooltip="toolb.swap.axes"  img="swap-axes" model="#{table01.extensions.swapAxes.buttonPressed}"/>
				  <wcf:separator/>
				  <wcf:scriptbutton model="#{table01.extensions.drillMember.enabled}"	 tooltip="toolb.navi.member" radioGroup="navi" id="drillMember"   img="navi-member"/>
				  <wcf:scriptbutton model="#{table01.extensions.drillPosition.enabled}" tooltip="toolb.navi.position" radioGroup="navi" id="drillPosition" img="navi-position"/>
				  <wcf:scriptbutton model="#{table01.extensions.drillReplace.enabled}"	 tooltip="toolb.navi.replace" radioGroup="navi" id="drillReplace"  img="navi-replace"/>
				  <!--wcf:scriptbutton model="#{table01.extensions.drillThrough.enabled}"  tooltip="toolb.navi.drillthru" id="drillThrough01"  img="navi-through"/-->
				  <wcf:separator/>
				  <wcf:scriptbutton id="chartButton01" tooltip="toolb.chart" img="chart" model="#{chart01.visible}"/>
				  <wcf:scriptbutton id="chartPropertiesButton01" tooltip="toolb.chart.config" img="chart-config" model="#{chartform01.visible}"/>
				  <wcf:separator/>
				  <wcf:scriptbutton id="printPropertiesButton01" tooltip="toolb.print.config" img="print-config" model="#{printform01.visible}"/>
				  <wcf:imgbutton id="printpdf" tooltip="toolb.print" img="print" href="../Print?cube=01&type=1"/>
				  <wcf:imgbutton id="printxls" tooltip="toolb.excel" img="excel" href="../Print?cube=01&type=0"/>
				</wcf:toolbar>

				<%-- render toolbar --%>
				<wcf:render ref="toolbar01" xslUri="/WEB-INF/jpivot/toolbar/htoolbar.xsl" xslCache="true"/>
				<p>

				<%-- if there was an overflow, show error message --%>
				<c:if test="${query01.result.overflowOccured}">
				  <p>
				  <strong style="color:red">Resultset overflow occured</strong>
				  <p>
				</c:if>

				<%-- render navigator --%>
				<wcf:render ref="navi01" xslUri="/WEB-INF/jpivot/navi/navigator.xsl" xslCache="true"/>

				<%-- sort properties --%>
				<wcf:render ref="sortform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

				<%-- chart properties --%>
				<wcf:render ref="chartform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

				<%-- print properties --%>
				<wcf:render ref="printform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

				<!-- render the table -->
				<p>
				<wcf:render ref="table01" xslUri="/WEB-INF/jpivot/table/mdxtable.xsl" xslCache="true"/>
				<p class="text8">
				<bean:message key="text.dynquery.slicer" bundle="queries"/>:
				<wcf:render ref="table01" xslUri="/WEB-INF/jpivot/table/mdxslicer.xsl" xslCache="true"/>

				<p>
				<!-- render chart -->
				<wcf:render ref="chart01" xslUri="/WEB-INF/jpivot/chart/chart.xsl" xslCache="true"/>

				<p>
			</form>

					</td>
				</tr>
				</table>
			</td>
		</tr>
	</table>
	</td>
