<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="conference.startVideoClient.title"/></title>

    <script type="text/javascript">
        var timer;
        $(function() {
            timer = setInterval(fetchEndpointId, 3000);
        });

        function fetchEndpointId() {
            document.getElementById('vidyoApplet').getEndpointId();
        }

        function endpointIdReceived(endpointId) {
            clearInterval(timer);

            $.post('${createLink(action: 'linkEndpoint')}', { endpointId: endpointId }, function(data) {
                waitForClient();
            }, 'json')
            .fail(function() {
                alert('Noget er gået galt. Prøv venligst forfra.');
            });
        }

        function clientNotRunning() {
            showInformationText('clientNotRunning');
        }

        function waitForClient() {
            showInformationText('waitingForClient');
            var clientStatus = document.getElementById('vidyoApplet').getClientStatus();
            if (clientStatus == 'VE_STATUS_BOUND') {
                $.post('${createLink(action: 'joinConference')}', function(data) {
                    waitForJoiningConference();
                }, 'json')
                .fail(function() {
                    alert('Noget er gået galt. Prøv venligst forfra.');
                });
            } else if (clientStatus == 'VE_STATUS_UNKNOWN') {
                showInformationText('clientError');
            } else {
                setTimeout(waitForClient, 500);
            }
        }

        function waitForJoiningConference() {
            showInformationText('waitingForJoiningConference');
            $.post('${createLink(action: 'conferenceJoined')}', function(data) {
                if (data.joined) {
                    $('#finishSettingUpConferenceForm').submit();
                } else {
                    setTimeout(waitForJoiningConference, 500);
                }
            }, 'json')
            .fail(function() {
                alert('Noget er gået galt. Prøv venligst forfra.');
            });
        }

        function showInformationText(shownElementId) {
            $('.informationText').hide();
            $('#' + shownElementId).show();
        }
    </script>
</head>
<body>
    <div id="fetchingEndpointId" class="informationText">
        <h1>Starter videoklient...</h1>
        <p>Klienten skal gerne starte op i løbet af få sekunder. Hvis det ikke sker, kan det skyldes flere ting:
        <ul class="bullet-list">
            <li>Video-klienten er ikke startet på din maskine.</li>
            <li>Der er ikke installeret Java i din browser.</li>
        </ul>
        Er du i tvivl, så kontakt din administrator.</p>
    </div>

    <div id="clientNotRunning" class="informationText" style="display:none">
        <h1>Video-klient kører ikke</h1>
        <p>Video-klienten skal køre i baggrunden. Er du i tvivl om hvordan dette sikres, så kontakt din administrator.</p>
    </div>

    <div id="waitingForClient" class="informationText" style="display:none">
        <h1>Venter på videoklient...</h1>
        <p>Videoklienten er i gang med at forbinde. Dette går normalt hurtigt, men kan i enkelte tilfælde tage lidt
        tid. Synes du det tager alt for lang tid, så kontakt din administrator</p>
    </div>

    <div id="waitingForJoiningConference" class="informationText" style="display:none">
        <h1>Venter på videokonference...</h1>
        <p>Er i gang med at starte selve mødet op. Dette går normalt hurtigt, men kan i enkelte tilfælde tage lidt
        tid. Synes du det tager alt for lang tid, så kontakt din administrator</p>
    </div>

    <div id="clientError" class="informationText" style="display:none">
        <h1>Fejl i video-klient</h1>
        <p>Video-klienten er i en ukendt tilstand. Prøv at genstarte den. Fortsætter fejlen, så kontakt din administrator.</p>
    </div>

    <g:form action="finishSettingUpConference" name="finishSettingUpConferenceForm"/>

    <video:applet clientParameters="${flash['clientParameters']}"
                  callback="endpointIdReceived"
                  clientNotRunningCallback="clientNotRunning"/>
</body>
</html>