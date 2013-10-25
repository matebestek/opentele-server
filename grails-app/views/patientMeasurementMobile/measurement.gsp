
<%@ page import="org.opentele.server.model.types.MeasurementFilterType; org.opentele.server.TimeFilter" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <meta name="layout" content="measurements_patient_mobil">
    <g:javascript src="jquery.js"></g:javascript>
</head>
<body>
<style>
    .no-close .ui-dialog-titlebar-close {
        display: none;
    }
    .ui-widget-header, .ui-widget-content {
        color: #777;
        background-color: white;
        border: 1px solid black;
    }
</style>
<script type="text/javascript">
    function underline(element) {
        element.css({ 'color': 'black', 'text-decoration': 'underline' });
    }
    $(function() {
        if('${params.filter}') {
            underline($('#${params.filter}'));
        } else {
            underline($('#${MeasurementFilterType.WEEK}'));
        }

        var timer = 0;
        $('.measurement').click(function() {
            var thiz = $(this);
            var time = $(this).data('time');
            var value = $(this).data('value');
            var before = $(this).data('before');
            var after = $(this).data('after');
            var control = $(this).data('control');
            var other = $(this).data('other');
            var details = $('#bloodsugar_details');
            details.html("Tidspunkt: "+time.toString()+
                    "<br />Værdi: " + value.toString()+
                    "<br />Før: " + (before ? "Ja" : "Nej") +
                    "<br />Efter: " + (after ? "Ja" : "Nej") +
                    "<br />Kontrol: " + (control ? "Ja" : "Nej") +
                    "<br />Andet: " + (other ? "Ja" : "Nej"));

            if( timer > 0 && details.dialog('isOpen') )
            {
                details.dialog( 'close' );
                details.removeAttr('click');
            }
            timer++;
            details.dialog({
                resizable: false,
                title: "Blodsukkermåling",
                dialogClass: 'no-close',
                position: { of: thiz }
            });
            details.click(function() { $(this).dialog('close'); });
            //clearTimeout(timer);
            //timer = setTimeout(function() { details.dialog( 'close' )}, 3000);
        });
    })
</script>

    <h1 style="text-align: center">${message(code: "patientMeasurements.label." + params.type)}</h1>
    <div class="period_adjustment">
        <a id="${MeasurementFilterType.WEEK}" href="${createLink(mapping: "patientMeasurementsTypeMobile", params:[type: "${params.type}", filter:"${MeasurementFilterType.WEEK}"])}">${message(code: 'time.filter.show.week')}</a> |
        <a id="${MeasurementFilterType.MONTH}" href="${createLink(mapping:"patientMeasurementsTypeMobile", params:[type: "${params.type}", filter:"${MeasurementFilterType.MONTH}"])}">${message(code: 'time.filter.show.month')}</a> |
        <a id="${MeasurementFilterType.QUARTER}" href="${createLink(mapping:"patientMeasurementsTypeMobile", params:[type: "${params.type}", filter:"${MeasurementFilterType.QUARTER}"])}">${message(code: 'time.filter.show.quarter')}</a> |
        <a id="${MeasurementFilterType.YEAR}" href="${createLink(mapping:"patientMeasurementsTypeMobile", params:[type: "${params.type}", filter:"${MeasurementFilterType.YEAR}"])}">${message(code: 'time.filter.show.year')}</a> |
        <a id="${MeasurementFilterType.ALL}" href="${createLink(mapping:"patientMeasurementsTypeMobile", params:[type: "${params.type}", filter:"${MeasurementFilterType.ALL}"])}">${message(code: 'time.filter.show.all')}</a>
    </div>

    <div class="measurements">
        <g:if test="${type == 'BLOODSUGAR'}">
            <g:render template="../measurement/bloodSugar" model="bloodSugarData" />
            <div id="bloodsugar_details" />
        </g:if>
        <g:elseif test="${tableData.empty}">
            <h1 class="information">Der er ingen målinger i det valgte tidsinterval.</h1>
        </g:elseif>
        <g:else>
            <table>
                <thead>
                <tr>
                    <th>Tidspunkt</th>
                    <th>${message(code: "enum.unit.${tableData.first().unit}")}</th>
                </tr>
                </thead>
                <tbody>
                    <g:each in="${tableData}" status="i" var="measurement">
                        <tr>
                            <td>${g.formatDate(date:measurement.time)}</td>
                            <td>${measurement}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </g:else>
    </div>
</body>
</html>