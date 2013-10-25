<td>Blodtryksmåling</td>
<td>
    <span>
        <g:hiddenField name="id" value="${measurement.id}"/>
        <g:hiddenField name="conferenceVersion" value="${measurement.conference.version}"/>
        <div>
            Systolisk (mmHg): <g:field type="string" name="systolic" value="${g.formatNumber(number: measurement.systolic, format: '0')}" class="${measurement.warningFields.contains('systolic') ? 'warning' : ''}"/>
        </div>
        <div name="systolic-error-message" style="display:none">
            Skal bestå af et heltal.
        </div>

        <div>
            Diastolisk (mmHg): <g:field type="string" name="diastolic" value="${g.formatNumber(number: measurement.diastolic, format: '0')}" class="${measurement.warningFields.contains('diastolic') ? 'warning' : ''}"/>
        </div>
        <div name="diastolic-error-message" style="display:none">
            Skal bestå af et heltal.
        </div>

        <div>
            Puls (slag i minuttet): <g:field type="string" name="pulse" value="${g.formatNumber(number: measurement.pulse, format: '0')}"/>
        </div>
        <div name="pulse-error-message" style="display:none">
            Skal bestå af et heltal.
        </div>
    </span>
</td>
<td><g:checkBox name="included" value="${measurement.included}"/></td>
