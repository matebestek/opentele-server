package opentele.server

class ConferenceCallJob {
    def conferenceStateService
    def concurrent = false

    def execute() {
        conferenceStateService.update(new Date())
    }
}
