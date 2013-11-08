<tmpl:drafts/measurement measurement="${measurement}" headline="Iltmætning">
    <tmpl:drafts/automaticValue name="saturation" title="Mætning (%)" value="${g.formatNumber(number: measurement.saturation, format:'0')}"/>
    <tmpl:drafts/automaticValue name="pulse" title="Puls (slag i minuttet)" value="${g.formatNumber(number: measurement.pulse, format: '0')}"/>
</tmpl:drafts/measurement>