<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<jp:mondrianQuery id="query01"
                  dataSource="billcheckout/reportDB"
				  catalogUri="/WEB-INF/queries/ep.xml"
				  dynResolver="br.com.auster.matsya.mondrian.i18n.ResouceEnabledDynamicLocalizer"
				  dynLocale="pt_BR">

select {[Measures].[TotalAmount]} on columns,
  {([Cycle].[All Cycles] , [Geographic].[All States], [Carrier].[All Carriers])} ON rows
from InvoiceTotals
</jp:mondrianQuery>


<c:set var="title01" scope="session">Totais de Faturas</c:set>
