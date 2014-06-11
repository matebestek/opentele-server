<g:javascript src="jqplot/jquery.jqplot.js"/>
<g:javascript src="jqplot/plugins/jqplot.canvasAxisLabelRenderer.js" />
<g:javascript src="jqplot/plugins/jqplot.canvasAxisTickRenderer.js" />
<g:javascript src="jqplot/plugins/jqplot.canvasTextRenderer.js" />
<g:javascript src="jqplot/plugins/jqplot.canvasOverlay.js" />
<g:javascript src="jqplot/plugins/jqplot.dateAxisRenderer.js" />
<g:javascript src="jqplot/plugins/jqplot.cursor.js" />
<g:javascript src="jqplot/plugins/jqplot.highlighter.js" />

<!--[if lt IE 9]>
<g:javascript src="jqplot/excanvas.js"/>
<![endif]-->

<script type="text/javascript">
    window.drawGraph = function(options) {
        $(function() {
            var containerId = options['containerId'];
            var title = options['title'];
            var series = options['series'];

            var formatStringX = options['formatStringX'];
            var ticksX = options['ticksX'];

            var minimumY = options['minimumY'];
            var maximumY = options['maximumY'];
            var labelY = options['labelY'];
            var ticksY = options['ticksY'];

            <g:if test="${request.showThresholds}">
                var alarmValues = options['alarmValues'];
                var warningValues = options['warningValues'];
            </g:if>
            <g:else>
                var alarmValues = [];
                var warningValues = [];
            </g:else>

            var seriesColors = options['seriesColors'];
            var highlighterFormatString = options['highlighterFormatString'];

            var singleClickFunction = options['singleClickFunction'];
            var doubleClickFunction = options['doubleClickFunction'];

            var container = $('#' + containerId);
            var showLegend = options['showLegend'];
            <g:if test="${request.showThresholds}">
                container.bind('jqplotDataClick', singleClickFunction);
                if (doubleClickFunction) {
                    container.bind('jqplotDblClick', doubleClickFunction);
                }
            </g:if>
            <g:if test="${request.showThresholds}">
                if(!highlighterFormatString) {
                    highlighterFormatString = '<table class="jqplot-highlighter-tooltip"><tr><td>%1$s %3$s, %5$s</td></tr><tr><td>${message(code:'patient.acknowledge.note')}:</td></tr><tr><td nowrap="wrap" style="width:160px;">%4$s</td></tr></table>';
                }
            </g:if>
            <g:else>
                highlighterFormatString = '<div>%5$s</div>';
            </g:else>

            var seriesConfig;

            if (seriesColors) {
                seriesConfig = [];
                for(var i = 0; i < seriesColors.length; i++) {
                    seriesConfig[i] = {
                        showLine: false,
                        color: seriesColors[i],
                        markerOptions: {size: 5}
                    }
                }

                seriesConfig[0].label = "${message(code: 'patient.overview.bloodsugar.beforemeal')}";
                if (seriesColors.length > 1) {
                    seriesConfig[1].label = "${message(code: 'patient.overview.bloodsugar.aftermeal')}";
                }
                if (seriesColors.length > 2) {
                    seriesConfig[2].label = "${message(code: 'patient.overview.bloodsugar.unknown')}";
                }
            } else {
                seriesConfig = [{},{}, { // Third series is for heart rate (pulse) in blood pressure graph
                    showLine: false,
                    markerOptions: { style: "x", size: 7, color: 'blue'}
                }];
            }

            var graph = $.jqplot (containerId, series, {
                title: title,
                gridPadding: { left: 40 },
                series: seriesConfig,

                axes: {
                    xaxis: {
                        pad: 1.2,
                        renderer: $.jqplot.DateAxisRenderer,
                        rendererOptions: {
                            tickRenderer: $.jqplot.CanvasAxisTickRenderer
                        },
                        ticks: ticksX,
                        tickOptions: {
                            formatString: formatStringX,
                            angle: -45
                        }
                    },
                    yaxis: {
                        min: minimumY,
                        max: maximumY,
                        label: labelY,
                        labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                        ticks: ticksY
                    }
                },
                legend:{
                    renderer: $.jqplot.EnhancedLegendRenderer,
                    show: showLegend,
                    location: 'nw'
                },
                highlighter: {
                    show: true,
                    sizeAdjust: 7.5,
                    tooltipOffset: 3,
                    fadeTooltip:true,
                    tooltipLocation: "s",
                    yvalues: 5,
                    formatString: highlighterFormatString
                },
                cursor: {
                    show: false
                }
            });

            function drawLine(color, y) {
                var canvas = $('#' + containerId + '>.jqplot-series-canvas')[0];
                if (canvas) {
                    var context = canvas.getContext("2d");
                    context.save();
                    context.strokeStyle = color;

                    var distanceFromLowerEdge = y - minimumY;
                    var canvasY = canvas.height - (distanceFromLowerEdge * canvas.height)/(maximumY - minimumY);
                    context.beginPath();
                    context.moveTo(0, canvasY);
                    context.lineTo(canvas.width, canvasY);
                    context.closePath();
                    context.stroke();

                    context.restore();
                }
            }

            function drawThresholdIndicators() {
                for (var i=0; i<alarmValues.length; i++) {
                    drawLine('red', alarmValues[i]);
                }
                for (var i=0; i<warningValues.length; i++) {
                    drawLine('yellow', warningValues[i]);
                }
            }
            // A bit hacky... if the div for the graph has style "display:none" when the graph is defined, JQPlot
            // does not plot the diagram. When the diagram is later shown, the code showing the graph must then
            // "trigger" the 'visibilityChanged' event to allow us to replot the graph.
            container.bind('visibilityChanged', function() {
                graph.replot();
                drawThresholdIndicators();
            });

            drawThresholdIndicators();
        });
    }
</script>
