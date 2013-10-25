<td>Vægt</td>
<td>
    <span>
        <g:hiddenField name="id" value="${measurement.id}"/>
        <g:hiddenField name="conferenceVersion" value="${measurement.conference.version}"/>
        <div>
            Vægt (kg): <g:field name="weight" type="string" value="${g.formatNumber(number: measurement.weight, format:'0.0', locale: 'DA')}"/>
        </div>
        <div name="weight-error-message" style="display:none">
            Skal bestå af et tal med højst én decimal. Brug komma.
        </div>
    </span>
</td>
<td><g:checkBox name="included" value="${measurement.included}"/></td>
