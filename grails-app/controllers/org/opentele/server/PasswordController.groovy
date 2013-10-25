package org.opentele.server

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.opentele.server.annotations.SecurityWhiteListController

@Secured(["IS_AUTHENTICATED_FULLY","IS_AUTHENTICATED_REMEMBERED"])
@SecurityWhiteListController
class PasswordController {
    static allowedMethods = [change:'GET',  update:'POST', changed: 'GET']

    def passwordService

    def change() {
        [command: new PasswordCommand()]
    }

    def update(PasswordCommand command) {
        passwordService.changePassword(command)
        if(command.hasErrors()) {
            withFormat {
                html {
                    render(view: 'change', model: [command: command])
                }
                json {
                    def errors = command.errors.fieldErrors.collect { [field: it.field, error: message(error: it)]}
                    render([status: 'error', errors: errors] as JSON)
                }
            }
        } else {
            def message = message(code: "password.changed.for.user", args: [command.user.username])
            withFormat {
                html {
                    flash.message= message
                    redirect(action: "changed")
                }
                json {
                    render([status: 'ok', message: message] as JSON)
                }
            }
        }
    }

    def changed() { }

}
