<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="conference.activeConference.title"/></title>

    <g:render template="popups"/>
</head>
<body>
    <h1>Videokonference i gang</h1>
    <p>Videokonference aktiv med nuværende patient.</p>

    <g:render template="unfinishedConferences" model="[conferences: unfinishedConferences, clinician: clinician]"/>

    <p>
        <g:form action="endConference">
            <fieldset class="buttons">
                <g:if test="${flash.conferenceToEdit}">
                    <g:link controller="conferenceMeasurements" action="show" id="${flash.conferenceToEdit}" class="button edit popup">Indtast målinger</g:link>
                </g:if>

                <g:submitButton name="close" class="delete" value="${message(code: 'conference.activeConference.close')}" />
            </fieldset>
        </g:form>
    </p>
</body>
</html>