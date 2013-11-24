<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="conference.conferenceEnded.title"/></title>

    <g:render template="popups"/>
</head>
<body>
    <h1><g:message code="conference.conferenceEnded.header"/></h1>

    <g:render template="unfinishedConferences" model="[conferences: unfinishedConferences]"/>

    <p><g:message code="conference.conferenceEnded.description"/></p>
</body>
</html>