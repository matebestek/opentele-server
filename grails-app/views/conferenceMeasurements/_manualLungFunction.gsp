<td>Lungefunktionstest</td>
<td>
    <span>
        <g:hiddenField name="id" value="${measurement.id}"/>
        <g:hiddenField name="conferenceVersion" value="${measurement.conference.version}"/>
        <div>
            FEV1 (L): <g:field type="string" name="fev1" value="${g.formatNumber(number: measurement.fev1, format:'0.00', locale: 'DA')}"/>
        </div>
        <div name="fev1-error-message" style="display:none">
            Skal bestå af et tal med højst to decimaler. Brug komma.
        </div>
    </span>
</td>
<td><g:checkBox name="included" value="${measurement.included}"/></td>
