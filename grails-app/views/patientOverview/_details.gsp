<g:if test="${unacknowledgedQuestionnaires.empty}">
    <g:message code="default.questionnaires.no.new" />
</g:if>
<g:else>
    <cq:renderResultTableForPatient patientID="${patient.id}"
                                    patientCompletedQuestionnaires="${unacknowledgedQuestionnaires}"
                                    questionPreferences="${questionPreferences}" withPrefs="${true}"
                                    unacknowledgedOnly="${true}" />
    <!-- Templates for Knockout.js -->
    <script id="prefRowTemplate" type="text/html">
        <tr id="prefQuestion" class="prefQuestion" data-bind="attr: {'selectedQuestionID': $root.getQuestionID($data)}">
            <td>
                <div>
                    <select data-bind="options: $root.questions, optionsText: 'text', value: $data.questionObj, optionsCaption: 'Vælg..'" onmouseover="tooltip.show('Vælg foretrukken værdi, denne kopieres til toppen af skemaet.');" onmouseout='tooltip.hide();'>
                    </select>
                    <!-- ko if: $root.notLastRow($data) -->
                    <button id="removeBtn" class="remove" data-bind="click: function(){ $data.remove(); }" onmouseover="tooltip.show('Fjern denne række');" onmouseout='tooltip.hide();'><img src="../images/cancel.png"/></button>
                    <!-- /ko -->
                </div>
            </td>
        </tr>
    </script>
    <script id="prefRowResTemplate" type="text/html">
        <!-- ko if: $data.resultObj() -->
        <tr class="prefResult" data-bind="html: $data.resultObj().text"></tr>
        <!-- /ko -->
    </script>
    <cq:overviewGraphs patient="${patient}"/>
</g:else>
