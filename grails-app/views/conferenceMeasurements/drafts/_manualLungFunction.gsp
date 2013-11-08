<tmpl:drafts/measurement measurement="${measurement}" headline="Spirometri">
    <tmpl:drafts/manualValue name="fev1" title="FEV1 (L)" value="${g.formatNumber(number: measurement.fev1, format:'0.00', locale: 'DA')}">
        Skal bestå af et tal med højst to decimaler. Brug komma.
    </tmpl:drafts/manualValue>
</tmpl:drafts/measurement>
