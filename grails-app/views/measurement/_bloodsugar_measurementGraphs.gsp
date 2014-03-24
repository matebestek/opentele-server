<%-- Please note: Requires the previous inclusion of _graphFunctions.gsp in the page, as it defines all the --%>
<%-- functionality needed by this file.                                                                     --%>

<%@ page import="grails.converters.JSON; org.opentele.server.model.NumericThreshold; org.opentele.server.model.types.MeasurementTypeName" %>
<g:if test="${measurement.type == MeasurementTypeName.BLOODSUGAR.name()}">
    <script type="text/javascript">

        var handleEmptySeries = function(series) { //JqPlot dislikes empty series, pushing 'null' fixes the problem.
            if(series[0].length == 0) {
                series[0].push(null);
            }

            if(series[1].length == 0) {
                series[1].push(null);
            }

            if(series[2].length == 0) {
                series[2].push(null);
            }

            return series
        }

        var resetMeasurementDate = function(date) {
            var newDate = new Date(date);

            newDate.setYear(2014);
            newDate.setMonth(0);
            if(newDate.getHours() < 8) {
                newDate.setDate(2);
            } else {
                newDate.setDate(1);
            }

            return newDate;
        };

        var formatDate = function(date) {
            return "" + date.getDate() + "/" + date.getMonth() + "/" + date.getFullYear();

        }

        var prepareMeasurement = function (series, index) {
            var date = resetMeasurementDate(series[index][0]);
            series[index][0] = date.getTime();
        };

        var prepareMeasurementSeries = function(beforeMealSeries, afterMealSeries, unknownMealSeries) {
            for(var i = 0; i < beforeMealSeries.length; i++) {
                prepareMeasurement(beforeMealSeries, i);
            }

            for(var i = 0; i < afterMealSeries.length; i++) {
                prepareMeasurement(afterMealSeries, i);
            }

            for(var i = 0; i < unknownMealSeries.length; i++) {
                prepareMeasurement(unknownMealSeries, i);
            }
            return [beforeMealSeries, afterMealSeries, unknownMealSeries]
        };


       window.drawGraph({
            containerId: '${measurement.type}-${patient.id}',
            title: '${title != null ? title : message(code: 'patient.graph.of', args: [message(code: "graph.legend.BLOODSUGAR"), patient.name.encodeAsHTML()])}',
            series: handleEmptySeries(${measurement.series as JSON}),

            seriesColors: ${measurement.seriesColors as JSON},
            showLegend: true,

            formatStringX: '${message(code:"graph.label.x.format.BLOODSUGAR")}',
            ticksX: ${measurement.ticksX as JSON},

            minimumY: ${measurement.minY},
            maximumY: ${measurement.maxY},
            labelY: '${message(code:"graph.label.y.BLOODSUGAR")}',
            ticksY: ${measurement.ticksY.collect { [it.value, it.text] } as JSON },
            alarmValues: ${measurement.alertValues as JSON},
            warningValues: ${measurement.warningValues as JSON},

            <g:if test="${patientIdForFullScreen != null}">
            doubleClickFunction: function (ev, seriesIndex, pointIndex, data) {
                document.location = '${createLink(mapping:"patientMeasurementGraph", params:[patientId: patientIdForFullScreen, measurementType: measurement.type])}' + window.location.search;
            },
            </g:if>
            singleClickFunction: function(ev, seriesIndex, pointIndex, data) {
                window.location.href = '${createLink(controller:"measurement", action:"show")}/' + ${measurement.seriesIds}[seriesIndex][pointIndex];
            }
        });



       var series = ${measurement.series as JSON};
       var seriesWithDatesReset = handleEmptySeries(prepareMeasurementSeries(series[0], series[1], series[2]));

        window.drawGraph({
            containerId: '${MeasurementTypeName.BLOODSUGAR.name()}-average-day-${patient.id}',
            title: '${title != null ? title : message(code: 'patient.graph.of', args: [message(code: "graph.legend.BLOODSUGAR_AVERAGE_DAY"), patient.name.encodeAsHTML()])}',
            series: seriesWithDatesReset,
            seriesColors: ${measurement.seriesColors as JSON},
            showLegend: true,

            formatStringX: '${message(code:"graph.label.x.format.BLOODSUGAR_AVERAGE_DAY")}',
            ticksX: [new Date("Jan 01, 2014 07:59:00"),
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
                new Date("Jan 02, 2014 00:00:00"),
                new Date("Jan 02, 2014 01:00:00"),
                new Date("Jan 02, 2014 02:00:00"),
                new Date("Jan 02, 2014 03:00:00"),
                new Date("Jan 02, 2014 04:00:00"),
                new Date("Jan 02, 2014 05:00:00"),
                new Date("Jan 02, 2014 06:00:00"),
                new Date("Jan 02, 2014 07:00:00"),
                new Date("Jan 02, 2014 08:00:00")],


            minimumY: ${measurement.minY},
            maximumY: ${measurement.maxY},
            labelY: '${message(code:"graph.label.y.BLOODSUGAR")}',
            ticksY: ${measurement.ticksY.collect { [it.value, it.text] } as JSON },
            alarmValues: ${measurement.alertValues as JSON},
            warningValues: ${measurement.warningValues as JSON},

            <g:if test="${patientIdForFullScreen != null}">
            doubleClickFunction: function (ev, seriesIndex, pointIndex, data) {
                document.location = '${createLink(mapping:"patientMeasurementGraph", params:[patientId: patientIdForFullScreen, measurementType: measurement.type])}' + window.location.search;
            },
            </g:if>
            singleClickFunction: function(ev, seriesIndex, pointIndex, data) {
                window.location.href = '${createLink(controller:"measurement", action:"show")}/' + ${measurement.seriesIds}[seriesIndex][pointIndex];
            },
            highlighterFormatString: '<table class="jqplot-highlighter-tooltip"><tr><td>%3$s, %5$s</td></tr><tr><td>${message(code:'patient.acknowledge.note')}:</td></tr><tr><td nowrap="wrap" style="width:160px;">%4$s</td></tr></table>'
        });
    </script>


    <!--  -->
</g:if>