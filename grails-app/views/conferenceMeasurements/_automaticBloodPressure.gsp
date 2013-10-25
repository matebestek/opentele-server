<td>Automatisk blodtryksmåling</td>
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
            Systolisk (mmHg): Venter...
        </div>
        <div class="waiting" style="display:${waitingDisplay}">
            Diastolisk (mmHg): Venter...
        </div>
        <div class="waiting" style="display:${waitingDisplay}">
            Puls (slag i minuttet): Venter...
        </div>

        <div class="loaded" style="display:${loadedDisplay}">
            Systolisk (mmHg):
            <span name="systolic" style="display:inline; float:none">
                ${g.formatNumber(number: measurement.systolic, format: '0')}
            </span>
        </div>
        <div class="loaded" style="display:${loadedDisplay}">
            Diastolisk (mmHg):
            <span name="diastolic" style="display:inline; float:none">
                ${g.formatNumber(number: measurement.diastolic, format: '0')}
            </span>
        </div>
        <div class="loaded" style="display:${loadedDisplay}">
            Puls (slag i minuttet):
            <span name="pulse" style="display:inline; float:none">
                ${g.formatNumber(number: measurement.pulse, format: '0')}
            </span>
        </div>
    </span>
</td>
<td><g:checkBox name="included" value="${measurement.included}"/></td>
