<tmpl:drafts/measurement measurement="${measurement}" headline="Automatisk blodtryksmåling">
    <tmpl:drafts/automaticValue name="systolic" title="Systolisk (mmHg)" value="${g.formatNumber(number: measurement.systolic, format: '0')}"/>
    <tmpl:drafts/automaticValue name="diastolic" title="Diastolisk (mmHg)" value="${g.formatNumber(number: measurement.diastolic, format: '0')}"/>
    <tmpl:drafts/automaticValue name="pulse" title="Puls (slag i minuttet)" value="${g.formatNumber(number: measurement.pulse, format: '0')}"/>
</tmpl:drafts/measurement>
