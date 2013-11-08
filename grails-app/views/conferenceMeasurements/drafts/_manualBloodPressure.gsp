<tmpl:drafts/measurement measurement="${measurement}" headline="Blodtryksmåling">
    <tmpl:drafts/manualValue name="systolic" title="Systolisk (mmHg)" value="${g.formatNumber(number: measurement.systolic, format: '0')}">
        Skal bestå af et heltal.
    </tmpl:drafts/manualValue>

    <tmpl:drafts/manualValue name="diastolic" title="Diastolisk (mmHg)" value="${g.formatNumber(number: measurement.diastolic, format: '0')}">
        Skal bestå af et heltal.
    </tmpl:drafts/manualValue>

    <tmpl:drafts/manualValue name="pulse" title="Puls (slag i minuttet)" value="${g.formatNumber(number: measurement.pulse, format: '0')}">
        Skal bestå af et heltal.
    </tmpl:drafts/manualValue>
</tmpl:drafts/measurement>