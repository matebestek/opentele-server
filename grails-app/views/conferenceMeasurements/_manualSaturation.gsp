<td>Iltmætning</td>
<td>
    <span>
        <g:hiddenField name="id" value="${measurement.id}"/>
        <g:hiddenField name="conferenceVersion" value="${measurement.conference.version}"/>
        <div>
            Mætning (%): <g:field name="saturation" type="string" value="${g.formatNumber(number: measurement.saturation, format:'0')}"/>
        </div>
        <div name="saturation-error-message" style="display:none">
            Skal bestå af et heltal.
        </div>

        <div>
            Puls (slag i minuttet): <g:field name="pulse" type="string" value="${g.formatNumber(number: measurement.pulse, format: '0')}"/>
        </div>
        <div name="pulse-error-message" style="display:none">
            Skal bestå af et heltal.
        </div>
    </span>
</td>
<td><g:checkBox name="included" value="${measurement.included}"/></td>
