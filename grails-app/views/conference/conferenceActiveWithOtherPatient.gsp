<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="conference.conferenceActiveWithOtherPatient.title"/></title>
</head>
<body>
    <h1>Videokonference i gang med anden patient</h1>
    <p>Du deltager p.t. i en videokonference med en anden patient.
    <g:form action="endConference">
        <fieldset class="buttons">
            <g:submitButton name="close" class="delete" value="${message(code: 'conference.conferenceActiveWithOtherPatient.close')}" />
        </fieldset>
    </g:form>
    </p>
</body>
</html>