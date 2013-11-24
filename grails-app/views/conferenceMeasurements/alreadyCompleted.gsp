<%@ page import="org.opentele.server.constants.Constants; org.opentele.server.model.ConferenceMeasurementDraftType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="conferenceMeasurements">

    <title><g:message code="conferenceMeasurement.alreadyClosed.title"/></title>
</head>

<body>
<h1><g:message code="conferenceMeasurement.alreadyClosed.title"/></h1>
<p><g:message code="conferenceMeasurement.alreadyClosed.description"/></p>
<p><g:link action="close"><g:message code="conferenceMeasurement.alreadyClosed.closeWindow"/></g:link></p>
</body>
</html>