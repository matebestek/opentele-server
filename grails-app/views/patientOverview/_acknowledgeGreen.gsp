<div style="display: inline-block; height: 40px; float: left;">
    <span><g:message code="patientOverview.${action}.green.label" /></span>
    <g:remoteLink controller="${controller}" action="${action}"
                  params="[withAutoMessage: 'false']" onComplete="location.reload(true)"
                  before="return confirm('${message(code: 'default.confirm.msg', args: [message(code: "confirm.context.msg.questionnaire.${action}")], default: 'Are you sure?')}')">
        <g:img dir="images" file="unacknowledged.png" data-tooltip='${message(code: "tooltip.acknowledge.${action}.without.message")}'/>
    </g:remoteLink>
    <g:remoteLink action="${action}" params="[withAutoMessage: 'true']"
                  onComplete="location.reload(true)"
                  before="return confirm('${message(code: 'default.confirm.msg', args: [message(code: "confirm.context.msg.questionnaire.${action}.and.send.messages")], default: 'Are you sure?')}')">
        <g:img dir="images" file="unacknowledgedWithAutoMessage.png" data-tooltip='${message(code: "tooltip.acknowledge.${action}.with.message")}'/>
    </g:remoteLink>
</div>
