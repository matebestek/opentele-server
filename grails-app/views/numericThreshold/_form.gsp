<%@ page import="org.opentele.server.model.StandardThresholdSet" %>
<table>
    <thead>
    <tr>
        <th><g:message code="default.threshold.alertHigh.label" default="Alert High"/></th>
        <th><g:message code="default.threshold.warningHigh.label" default="Warning High"/></th>
        <th><g:message code="default.threshold.warningLow.label" default="Warning Low"/></th>
        <th><g:message code="default.threshold.alertLow.label" default="Alert Low"/></th>
    </tr>
    </thead>
    <tbody>
    <tr class="thresholds">
        <td>
            <g:field type="text" name="alertHigh"
                     value="${formatNumber(number: standardThresholdInstance.alertHigh, format: "0.0")}"/>
            <r:img uri="/images/remove-icon.png"/>
        </td>
        <td>
            <g:field type="text" name="warningHigh"
                     value="${formatNumber(number: standardThresholdInstance.warningHigh, format: "0.0")}"/>
             <r:img uri="/images/remove-icon.png"/>
        </td>
        <td>
            <g:field type="text" name="warningLow"
                     value="${formatNumber(number: standardThresholdInstance.warningLow, format: "0.0")}"/>
             <r:img uri="/images/remove-icon.png"/>
        </td>
        <td>
            <g:field type="text" name="alertLow"
                     value="${formatNumber(number: standardThresholdInstance.alertLow, format: "0.0")}"/>
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
                if (!input.val()) {
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
