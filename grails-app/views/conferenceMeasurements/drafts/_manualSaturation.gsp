<tmpl:drafts/measurement measurement="${measurement}" headline="Iltmætning">
    <tmpl:drafts/manualValue name="saturation" title="Mætning (%)" value="${g.formatNumber(number: measurement.saturation, format:'0')}">
        Skal bestå af et heltal.
    </tmpl:drafts/manualValue>

    <tmpl:drafts/manualValue name="pulse" title="Puls (slag i minuttet)" value="${g.formatNumber(number: measurement.pulse, format: '0')}">
        Skal bestå af et heltal.
    </tmpl:drafts/manualValue>
</tmpl:drafts/measurement>