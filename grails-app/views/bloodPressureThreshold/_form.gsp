<%@ page import="org.opentele.server.model.BloodPressureThreshold" %>


<table>
    <thead>
    <tr>
        <th><g:message code="default.threshold.type.label" default="Type"/></th>
        <th><g:message code="default.threshold.alertHigh.label" default="Alert High"/></th>
        <th><g:message code="default.threshold.warningHigh.label" default="Warning High"/></th>
        <th><g:message code="default.threshold.warningLow.label" default="Warning Low"/></th>
        <th><g:message code="default.threshold.alertLow.label" default="Alert Low"/></th>
    </tr>
    </thead>
    <tbody>
    <tr class="thresholds">
        <th><g:message code="default.threshold.diastolic.label"/></th>
        <td>
            <g:field type="text" name="diastolicAlertHigh"
                     value="${formatNumber(number: standardThresholdInstance.diastolicAlertHigh, format: "0.0")}"/>
             <r:img uri="/images/remove-icon.png"/>
        </td>
        <td>
            <g:field type="text" name="diastolicWarningHigh"
                     value="${formatNumber(number: standardThresholdInstance.diastolicWarningHigh, format: "0.0")}"/>
             <r:img uri="/images/remove-icon.png"/>
        </td>
        <td>
            <g:field type="text" name="diastolicWarningLow"
                     value="${formatNumber(number: standardThresholdInstance.diastolicWarningLow, format: "0.0")}"/>
             <r:img uri="/images/remove-icon.png"/>
        </td>
        <td>
            <g:field type="text" name="diastolicAlertLow"
                     value="${formatNumber(number: standardThresholdInstance.diastolicAlertLow, format: "0.0")}"/>
             <r:img uri="/images/remove-icon.png"/>
        </td>
    </tr>
    <tr class="thresholds">
        <th><g:message code="default.threshold.systolic.label"/></th>
        <td>
            <g:field type="text" name="systolicAlertHigh"
                     value="${formatNumber(number: standardThresholdInstance.systolicAlertHigh, format: "0.0")}"/>
             <r:img uri="/images/remove-icon.png"/>
        </td>
        <td>
            <g:field type="text" name="systolicWarningHigh"
                     value="${formatNumber(number: standardThresholdInstance.systolicWarningHigh, format: "0.0")}"/>
             <r:img uri="/images/remove-icon.png"/>
        </td>
        <td>
            <g:field type="text" name="systolicWarningLow"
                     value="${formatNumber(number: standardThresholdInstance.systolicWarningLow, format: "0.0")}"/>
             <r:img uri="/images/remove-icon.png"/>
        </td>
        <td>
            <g:field type="text" name="systolicAlertLow"
                     value="${formatNumber(number: standardThresholdInstance.systolicAlertLow, format: "0.0")}"/>
             <r:img uri="/images/remove-icon.png"/>
        </td>
    </tr>

    </tbody>
</table>
<script lang="javascript">
    $('.thresholds')
            .on('focus', 'input[type="text"]', function () {
                var input = $(this);
                input.css('background-color', '');
            })
            .on('blur', 'input[type="text"]', function () {
                var input = $(this);
                if (!input.val() || input.val() === "") {
                    input.css('background-color', "#C2C2C2")
                }
            })
            .on('click', 'img', function () {
                var input = $(this).prev();
                input.css('background-color', '#C2C2C2').val('')
            })
            .find('input[type="text"]')
            .each(function () {
                var input = $(this);
                if (!input.val() || input.val() == "") {
                    input.css('background-color', "#C2C2C2")
                }
            });
</script>
