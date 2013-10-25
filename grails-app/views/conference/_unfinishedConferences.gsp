<g:if test="${!conferences.empty}">
    <g:each in="${conferences.sort { it.createdDate }.reverse()}" var="conference">
        <p class="buttons">
            Samtalen med ${conference.clinician.name} <g:formatDate date="${conference.createdDate}"/> er ikke afsluttet
            <g:if test="${conference.clinician == clinician}">
                <g:link controller="conferenceMeasurements" action="show" id="${conference.id}" class="edit popup" style="float:none">Fortsæt redigering</g:link>
            </g:if>
        </p>
    </g:each>
</g:if>
