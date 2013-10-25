<%@ page import="org.opentele.server.constants.Constants; org.opentele.server.model.ConferenceMeasurementDraftType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="conferenceMeasurements">

    <title>Indtastning af målinger</title>

    <r:script>
    $(function() {
        $('#measurementDraftType').change(function() {
            var type = $(this).val();
            var automatic = false;
            if (stringStartsWith(type, 'AUTOMATIC_')) {
                type = type.substring(10);
                automatic = true;
            } else if (stringStartsWith(type, 'MANUAL_')) {
                type = type.substring(7);
            }
            var tr = $('<tr/>').appendTo('#measurements');
            tr.load('${createLink(action: 'loadForm')}', {id: ${conference.id}, type: type, automatic: automatic}, function() {
                $('#measurementDraftType').val('null');
            });
        });

        $('body').on('change', 'input', function() {
            var tr = $(this).parents('tr');
            var inputFields = $('input', tr);

            addToUpdateQueue(inputFields);
        });

        setTimeout(checkAutomaticMeasurements, 2000);

        function checkAutomaticMeasurements() {
            var promises = $.map($.find('.waiting-measurement'), function(element) {
                return startUpdatingWaitingElement(element);
            });
            $.when(promises).always(function() {
                setTimeout(checkAutomaticMeasurements, 2000);
            });
        }

        function startUpdatingWaitingElement(element) {
            var measurementId = $(element).find("input[name='id']").val();
            return $.ajax('${createLink(action: 'loadAutomaticMeasurement')}?id=' + measurementId)
            .success(function(data) {
                if (data !== '') {
                    updateWaitingElement(element, data);
                }
            });
        }

        function updateWaitingElement(element, data) {
            $.map(data, function(value, key) {
                $(element).find("span[name='" + key + "']").html(data[key]);
            });
            $(element).find('.waiting').hide();
            $(element).find('.loaded').show();
            $(element).removeClass('waiting-measurement');
        }

        var updateQueue = [];
        var updateInProgress = false;

        function addToUpdateQueue(inputFields) {
            updateQueue.push(inputFields);
            processUpdateQueue();
        }

        function processUpdateQueue() {
            if (!updateInProgress && updateQueue.length > 0) {
                var inputFields = updateQueue.shift();
                setUpdateInProgress(true);

                $.post('${createLink(action: 'updateMeasurement')}', inputFields.serializeArray(), function(data) {
                    updateConferenceVersionEverywhere(data['conferenceVersion']);
                    setFieldStyles(inputFields, data['warnings'], data['errors']);
                }, 'json')
                .fail(function() {
                    alert('Noget er gået galt! Luk venligst vinduet og åbn det på ny.');
                })
                .always(function() {
                    setUpdateInProgress(false);
                    processUpdateQueue();
                });
            }
        }

        function setUpdateInProgress(inProgress) {
            updateInProgress = inProgress;
            setSubmitDisabled(inProgress);
        }

        function setFieldStyles(inputFields, warningFields, errorFields) {
            for (var i=0; i < inputFields.length; i++) {
                var inputField = inputFields[i];
                var name = inputField.name;
                var isError = arrayContains(errorFields, name);
                var isWarning = !isError && arrayContains(warningFields, name);
                var errorMessageField = $(inputField).closest('td').find('div[name=' + name + '-error-message]');

                if (isError) {
                    $(inputField).addClass('error');
                    errorMessageField.slideDown();
                } else {
                    $(inputField).removeClass('error');
                    errorMessageField.slideUp();
                }

                if (isWarning) {
                    $(inputField).addClass('warning');
                } else {
                    $(inputField).removeClass('warning');
                }
            }
        }

        function updateConferenceVersionEverywhere(newVersion) {
            $("input[name='conferenceVersion']").val(newVersion);
        }

        function setSubmitDisabled(disabled) {
            var hasWarnings = $('.warning').length > 0;
            var hasErrors = $('.error').length > 0;
            if (hasWarnings || hasErrors) {
                disabled = true
            }
            $("input[name='confirm']").attr('disabled', disabled);
        }

        function arrayContains(array, value) {
            return $.inArray(value, array ) != -1;
        }

        function stringStartsWith(string, prefix) {
            return string.indexOf(prefix) == 0;
        }
    });
    </r:script>
</head>

<body>
<h1>Målinger</h1>
<p>
    <span id="patent_name">${session[Constants.SESSION_NAME]} </span>
    <span id="patient_cpr"><g:message code="main.SSN"/>: ${session[Constants.SESSION_CPR]} </span>
</p>
<table>
    <thead>
    <tr>
        <th>
            Målingstype
        </th>
        <th>
            Værdi(er)
        </th>
        <th>
            Medtages
        </th>
    </tr>
    </thead>
    <tbody id="measurements">
        <g:each in="${measurementDrafts.sort { it.id }}" var="measurementDraft">
            <tr>
                <g:if test="${measurementDraft.type == ConferenceMeasurementDraftType.BLOOD_PRESSURE && !measurementDraft.automatic}">
                    <g:render template="manualBloodPressure" model="[measurement: measurementDraft]"/>
                </g:if>
                <g:elseif test="${measurementDraft.type == ConferenceMeasurementDraftType.BLOOD_PRESSURE && measurementDraft.automatic}">
                    <g:render template="automaticBloodPressure" model="[measurement: measurementDraft]"/>
                </g:elseif>
                <g:elseif test="${measurementDraft.type == ConferenceMeasurementDraftType.LUNG_FUNCTION && !measurementDraft.automatic}">
                    <g:render template="manualLungFunction" model="[measurement: measurementDraft]"/>
                </g:elseif>
                <g:elseif test="${measurementDraft.type == ConferenceMeasurementDraftType.LUNG_FUNCTION && measurementDraft.automatic}">
                    <g:render template="automaticLungFunction" model="[measurement: measurementDraft]"/>
                </g:elseif>
                <g:elseif test="${measurementDraft.type == ConferenceMeasurementDraftType.SATURATION && !measurementDraft.automatic}">
                    <g:render template="manualSaturation" model="[measurement: measurementDraft]"/>
                </g:elseif>
                <g:elseif test="${measurementDraft.type == ConferenceMeasurementDraftType.WEIGHT && !measurementDraft.automatic}">
                    <g:render template="manualWeight" model="[measurement: measurementDraft]"/>
                </g:elseif>
                <g:else>
                    <td colspan="3">Ukendt målingstype: ${measurementDraft.type}</td>
                </g:else>
            </tr>
        </g:each>
    </tbody>
    <tfoot>
        <tr>
            <td colspan="3">
                Tilføj måling:
                <g:select name="measurementDraftType" valueMessagePrefix="conferenceMeasurements.measurementType" from="['MANUAL_BLOOD_PRESSURE', 'AUTOMATIC_BLOOD_PRESSURE', 'MANUAL_LUNG_FUNCTION', 'AUTOMATIC_LUNG_FUNCTION', 'MANUAL_SATURATION', 'MANUAL_WEIGHT']" noSelection="[null: '']"/>

                <g:form action="confirm">
                    <g:hiddenField name="id" value="${conference.id}"/>
                    <g:hiddenField name="conferenceVersion" value="${conference.version}"/>
                    <g:submitButton name="confirm" value="Gem og luk"/>
                </g:form>
            </td>
        </tr>
    </tfoot>
</table>
</body>
</html>