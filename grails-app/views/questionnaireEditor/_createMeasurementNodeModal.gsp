<%@ page import="org.opentele.server.model.types.MeterTypeName" %>
<div id="createMeasurementNodeModal" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">
            <g:message code="questionnaireEditor.modal.title.create.measurement"/>
        </h3>
    </div>
    <div class="modal-body">
        <form>
            <fieldset>
                <label><g:message code="questionnaireEditor.modal.shortText"/> </label>
                <input type="text" id="shortText" placeholder="${g.message(code: 'questionnaireEditor.modal.shortText')}" class="span5">

                <label><g:message code="questionnaireEditor.modal.headline"/> </label>
                <textarea rows="2" type="text" id="headline" placeholder="${g.message(code: 'questionnaireEditor.modal.headline')}" class="span5"></textarea>

                <label><g:message code="questionnaireEditor.modal.measurementType"/> </label>
                <g:select name="measurementType" from="${MeterTypeName.values().collect{it}}" valueMessagePrefix="questionnaireEditor.metertype" class="span5"/>

                <span class="help-block"><g:message code="questionnaireEditor.modal.measurementForm"/></span>
                <label class="radio">
                    <input type="radio" name="measurementForm" id="automatic" value="automatic">
                    <g:message code="questionnaireEditor.modal.measurementForm.automatic"/>
                </label>
                <label class="radio">
                    <input type="radio" name="measurementForm" id="manual" value="manual">
                    <g:message code="questionnaireEditor.modal.measurementForm.manual"/>
                </label>
                <label class="radio">
                    <input type="radio" name="measurementForm" id="simulated" value="simulated">
                    <g:message code="questionnaireEditor.modal.measurementForm.simulated"/>
                </label>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <button id="cancel" class="btn" ><g:message code="questionnaireEditor.modal.cancel"/></button>
        <button id="create" class="btn btn-primary disabled" disabled="true"><g:message code="questionnaireEditor.modal.create"/></button>
    </div>
</div>

<script type="text/javascript">
    //Page validation functionality: Must enter headline
    $(new function() {
        var createButton = $('#createMeasurementNodeModal #create');
        $('#createMeasurementNodeModal #headline').keyup(function() {
            if($(this).val().length > 0) {
                createButton.removeClass('disabled');
                createButton.attr('disabled', false);
            } else {
                createButton.addClass('disabled');
                createButton.attr('disabled', true);
            }
        });
    });
</script>