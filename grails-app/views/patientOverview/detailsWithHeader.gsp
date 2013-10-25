<div class="patientEntry" id="${patient}">
    <div class="questionnaireList" id="${patient.id}">
        <cq:renderOverviewForPatient patient="${patient}" completedQuestionnaires="${unacknowledgedQuestionnaires}" patientNotes="${notes}"/>
        <div class="questionnaireListInner">
            <tmpl:details/>
        </div>
    </div>
</div>
