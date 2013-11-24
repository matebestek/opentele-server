<div style="display: inline-block; height: 40px; float: left;">
    <span><g:message code="patientOverview.acknowledgeAllGreen" /></span>
    <g:remoteLink controller="patientOverview" action="acknowledgeAllForAll"
                  params="[withAutoMessage: 'false']" onComplete="location.reload(true)"
                  before="return confirm('${message(code: 'patientOverview.acknowledgeAllForAll.confirm')}')">
        <g:img dir="images" file="unacknowledged.png" data-tooltip='${message(code: "patientOverview.acknowledgeAllForAllWithoutMessage")}'/>
    </g:remoteLink>
    <g:remoteLink action="acknowledgeAllForAll" params="[withAutoMessage: 'true']"
                  onComplete="location.reload(true)"
                  before="return confirm('${message(code: 'patientOverview.acknowledgeAllForAllWithMessage.confirm')}')">
        <g:img dir="images" file="unacknowledgedWithAutoMessage.png" data-tooltip='${message(code: "patientOverview.acknowledgeAllForAllWithMessage")}'/>
    </g:remoteLink>
</div>
