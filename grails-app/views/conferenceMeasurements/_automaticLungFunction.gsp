<td>Automatisk spirometri</td>
<%
    String tdClass = measurement.waiting ? 'waiting-measurement' : 'loaded-measurement'
    String waitingDisplay = measurement.waiting ? 'block' : 'none'
    String loadedDisplay = measurement.waiting ? 'none' : 'block'
%>
<td class="${tdClass}">
    <span>
        <g:hiddenField name="id" value="${measurement.id}"/>
        <g:hiddenField name="conferenceVersion" value="${measurement.conference.version}"/>
        <div class="waiting" style="display:${waitingDisplay}">
            FEV1 (L): Venter...
        </div>
        <div class="loaded" style="display:${loadedDisplay}">
            FEV1 (L):
            <span name="fev1" style="display:inline; float:none">
                ${g.formatNumber(number: measurement.fev1, format:'0.00', locale: 'DA')}
            </span>
        </div>
    </span>
</td>
<td><g:checkBox name="included" value="${measurement.included}"/></td>
