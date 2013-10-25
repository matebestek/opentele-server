<%@ page import="org.opentele.server.model.types.DataType; org.opentele.server.model.types.MeterTypeName" %>
<div id="createInputNodeModal" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">
            <g:message code="questionnaireEditor.modal.title.create.input"/>
        </h3>
    </div>
    <div class="modal-body">
        <form>
            <fieldset>
                <label><g:message code="questionnaireEditor.modal.shortText"/> </label>
                <input type="text" id="shortText" placeholder="${g.message(code: 'questionnaireEditor.modal.shortText')}" class="span5">

                <label><g:message code="questionnaireEditor.modal.question"/> </label>
                <input type="text" id="question" placeholder="${g.message(code: 'questionnaireEditor.modal.question')} " class="span5">

                <label><g:message code="questionnaireEditor.modal.answertype"/> </label>
                <g:select name="dataType" from="${[DataType.FLOAT, DataType.INTEGER, DataType.STRING, DataType.BOOLEAN]}"  valueMessagePrefix="questionnaireEditor.dataType" class="span5"/>
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
        var createButton = $('#createInputNodeModal #create');
        $('#createInputNodeModal #question').keyup(function() {
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