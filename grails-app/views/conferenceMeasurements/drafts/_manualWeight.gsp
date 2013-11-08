<tmpl:drafts/measurement measurement="${measurement}" headline="Vægt">
    <tmpl:drafts/manualValue name="weight" title="Vægt (kg)" value="${g.formatNumber(number: measurement.weight, format:'0.0', locale: 'DA')}">
        Skal bestå af et tal med højst én decimal. Brug komma.
    </tmpl:drafts/manualValue>
</tmpl:drafts/measurement>
