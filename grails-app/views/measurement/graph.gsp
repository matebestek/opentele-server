<!doctype html>
<html>
<head>
    <meta name="layout" content="fullScreen">
    <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/>  ${patient.name.encodeAsHTML()}</title>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.jqplot.css')}" type="text/css">
    <g:javascript src="jquery.js"/>
    <g:render template="graphFunctions"/>
	<g:render template="measurementGraph" model='[patient: patient, measurement: measurement, title: message(code: "patient.graph.of",args: [message(code: "graph.legend.${measurement.type}"), patient.name.encodeAsHTML()])]'/>
    <style type="text/css">
   		body { height: 100% }
   		.front { position: absolute; z-index: 20 }
        .fullScreen { position: absolute; left: 0; right: 0; top: 0; bottom: 0; }
   		.fullScreenGraph { position: absolute; left: 3em; right: 0; bottom: 0; height:100%; }
	</style>
</head>

<body>
	<div class="front">
		<a href='${createLink(mapping: "patientGraphs", params: [patientId: patient.id])}'><g:message code="default.back"/></a>
	</div>
	<div class="content scaffold-show fullScreen" role="main">
	    <!--[if lte IE 7]>
		<i>Der er desværre ikke support for grafer i Internet Explorer 7 eller mindre</i>
		<![endif]-->
        <![if gt IE 7]>
        <div id="${measurement.type}-${patient.id}" class="fullScreenGraph"></div>
        <![endif]>
	</div>
</body>
</html>
