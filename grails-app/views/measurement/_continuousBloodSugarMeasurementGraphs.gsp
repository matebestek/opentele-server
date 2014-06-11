<%-- Please note: Requires the previous inclusion of _graphFunctions.gsp in the page, as it defines all the --%>
<%-- functionality needed by this file.                                                                     --%>

<%@ page import="grails.converters.JSON; org.opentele.server.model.NumericThreshold; org.opentele.server.model.types.MeasurementTypeName" %>
<g:if test="${measurement.type == MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT.name()}">
    <script type="text/javascript">

        var resetMeasurementDate = function(date) {
            var newDate = new Date(date);

            newDate.setYear(2014);
            newDate.setMonth(0);
            newDate.setDate(1);
            return newDate;
        };

        var formatDate = function(date) {
            return "" + date.getDate() + "/" + date.getMonth() + "/" + date.getFullYear();

        }

        var prepareMeasurementSeries = function(series) {
            for(var i = 0; i < series[0].length; i++) {
                var date = resetMeasurementDate(series[0][i][0]);
                series[0][i][0] = date.getTime();
            }
            return series;
        };

    window.drawGraph({
            containerId: '${MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT}-${patient.id}',
            title: '${title != null ? title : message(code: 'patient.graph.of', args: [message(code: "graph.legend.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT"), patient.name.encodeAsHTML()])}',
            series: ${measurement.series as JSON},

            seriesColors: [''],
            showLegend: false,

            formatStringX: '${message(code:"graph.label.x.format.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT")}',
            ticksX: ${measurement.ticksX as JSON},

            minimumY: ${measurement.minY},
            maximumY: ${measurement.maxY},
            labelY: '${message(code:"graph.label.y.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT")}',
            ticksY: ${measurement.ticksY.collect { [it.value, it.text] } as JSON },
            alarmValues: ${measurement.alertValues as JSON},
            warningValues: ${measurement.warningValues as JSON},

            <g:if test="${patientIdForFullScreen != null}">
            doubleClickFunction: function (ev, seriesIndex, pointIndex, data) {
                document.location = '${createLink(mapping:"patientMeasurementGraph", params:[patientId: patientIdForFullScreen, measurementType: measurement.type])}' + window.location.search;
            },
            </g:if>
            singleClickFunction: function(ev, seriesIndex, pointIndex, data) {
            }
        });



       var series = ${measurement.series as JSON};
       var seriesWithDatesReset = prepareMeasurementSeries(series);

        window.drawGraph({
            containerId: '${MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT.name()}-average-day-${patient.id}',
            title: '${title != null ? title : message(code: 'patient.graph.of', args: [message(code: "graph.legend.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT_AVERAGE_DAY"), patient.name.encodeAsHTML()])}',
            series: seriesWithDatesReset,
            seriesColors: [''],
            showLegend: false,

            formatStringX: '${message(code:"graph.label.x.format.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT_AVERAGE_DAY")}',
            ticksX: [new Date("Jan 01, 2014 00:00:00"),
                new Date("Jan 01, 2014 01:00:00"),
                new Date("Jan 01, 2014 02:00:00"),
                new Date("Jan 01, 2014 03:00:00"),
                new Date("Jan 01, 2014 03:00:00"),
                new Date("Jan 01, 2014 04:00:00"),
                new Date("Jan 01, 2014 05:00:00"),
                new Date("Jan 01, 2014 06:00:00"),
                new Date("Jan 01, 2014 07:00:00"),
                new Date("Jan 01, 2014 08:00:00"),
                new Date("Jan 01, 2014 09:00:00"),
                new Date("Jan 01, 2014 10:00:00"),
                new Date("Jan 01, 2014 11:00:00"),
                new Date("Jan 01, 2014 12:00:00"),
                new Date("Jan 01, 2014 13:00:00"),
                new Date("Jan 01, 2014 14:00:00"),
                new Date("Jan 01, 2014 15:00:00"),
                new Date("Jan 01, 2014 16:00:00"),
                new Date("Jan 01, 2014 17:00:00"),
                new Date("Jan 01, 2014 18:00:00"),
                new Date("Jan 01, 2014 19:00:00"),
                new Date("Jan 01, 2014 20:00:00"),
                new Date("Jan 01, 2014 21:00:00"),
                new Date("Jan 01, 2014 22:00:00"),
                new Date("Jan 01, 2014 23:00:00"),
                new Date("Jan 01, 2014 23:59:59")],


            minimumY: ${measurement.minY},
            maximumY: ${measurement.maxY},
            labelY: '${message(code:"graph.label.y.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT_AVERAGE_DAY")}',
            ticksY: ${measurement.ticksY.collect { [it.value, it.text] } as JSON },
            alarmValues: ${measurement.alertValues as JSON},
            warningValues: ${measurement.warningValues as JSON},

            <g:if test="${patientIdForFullScreen != null}">
            doubleClickFunction: function (ev, seriesIndex, pointIndex, data) {
                document.location = '${createLink(mapping:"patientMeasurementGraph", params:[patientId: patientIdForFullScreen, measurementType: measurement.type])}' + window.location.search;
            },
            </g:if>
            singleClickFunction: function(ev, seriesIndex, pointIndex, data) {
            },
            highlighterFormatString: '<table class="jqplot-highlighter-tooltip"><tr><td>%3$s, %5$s</td></tr><tr><td>${message(code:'patient.acknowledge.note')}:</td></tr><tr><td nowrap="wrap" style="width:160px;">%4$s</td></tr></table>'
        });
    </script>
</g:if>