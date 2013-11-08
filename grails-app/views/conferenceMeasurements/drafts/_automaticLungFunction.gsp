<tmpl:drafts/measurement measurement="${measurement}" headline="Automatisk spirometri">
    <tmpl:drafts/automaticValue name="fev1" title="FEV1 (L)" value="${g.formatNumber(number: measurement.fev1, format:'0.00', locale: 'DA')}"/>
</tmpl:drafts/measurement>
