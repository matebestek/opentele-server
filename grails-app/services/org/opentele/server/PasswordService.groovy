package org.opentele.server

import grails.util.Environment
import org.codehaus.groovy.grails.web.util.WebUtils
import org.opentele.server.model.User
import org.springframework.context.ApplicationContext
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException

import javax.servlet.http.HttpServletResponse

import static java.util.Calendar.SECOND

class PasswordService {

    def grailsApplication
    def springSecurityService
    def authenticationManager

    String passwordDictionaryPath = "/WEB-INF/resources/passwordDictionary.txt"
    Integer randomNumberRange = 100;  // range from 0..<100
    private List<String> words = []

    String generateTempPassword() {
        def number = getRandomNumberPadded()
        def word1 = getRandomWord()
        def word2 = getRandomWord()
        return "$word1$number$word2"
    }

    boolean registerBadPasswordAttempt(String username) {
        User user = User.findByUsername(username)

        if(!user) {
            return
        }

        def passwordMaxAttempts = grailsApplication.config.passwordMaxAttempts ?: 3
        def grace = grailsApplication.config.passwordRetryGracePeriod ?: 120
        def thisAttempt = new Date()
        Date latestPlusGrace = user.modifiedDate?.clone() ?: new Date()
        latestPlusGrace[SECOND] += grace

        if(thisAttempt.before(latestPlusGrace)) {
            user.badLoginAttemps++
        } else {
            user.badLoginAttemps = 1
        }

        if (user.badLoginAttemps >= passwordMaxAttempts) {
            user.accountLocked = true
            return true
        }
        return false
    }

    def registerGoodPasswordAttempt(String username) {
        User user = User.findByUsername(username)
        if(!user) {
            return
        }

        user.badLoginAttemps = 0
    }

    void unlockAccount(User user) {
        user.accountLocked = false
        user.badLoginAttemps = 0
    }

    void resetPassword(User user) {
        def tempPassword = generateTempPassword()
        user.badLoginAttemps = 0
        user.password = tempPassword
        user.cleartextPassword = tempPassword
        user.accountLocked = false
        user.save(flush: true)

        HttpServletResponse response = getResponse()

        response.addHeader('PasswordReset', 'true') //Tell the client that the password has been reset.
    }

    private HttpServletResponse getResponse() {
        def utils = WebUtils.retrieveGrailsWebRequest()
        def response = utils.getCurrentResponse()
        response
    }



    private getRandomWord() {
        words[new Random().nextInt(words.size())]
    }

    private getRandomNumberPadded() {
        def maxLength = (randomNumberRange - 1).toString().size()
        return new Random().nextInt(randomNumberRange).toString().padLeft(maxLength, '0')
    }

    public initialize(ApplicationContext applicationContext) {
        log.debug("Loading words for temp passwords....")
        def dictionaryFile = applicationContext.getResource(passwordDictionaryPath).file
        words = dictionaryFile.readLines()
        log.debug("... ${words.size()} words loaded.")
    }

    def changePassword(PasswordCommand passwordCommand) {
        User currentUser = springSecurityService.currentUser
        passwordCommand.user = currentUser
        if (passwordCommand.validate() && oldPasswordIsCorrect(passwordCommand) && isNotHelle(passwordCommand)) {
            currentUser.cleartextPassword = null
            currentUser.password = passwordCommand.password
            currentUser.save(flush: true)
        }

    }

    //Don't allow password changes for HelleAndersen on datamon_test and on development
    private isNotHelle(PasswordCommand passwordCommand) {
        if (grailsApplication.metadata["app.version"] && Environment?.current?.name in ["datamon_test","development"]) {
            if (passwordCommand.user.username == "HelleAndersen") {
                passwordCommand.errors.rejectValue('user', "passwordCommand.user.passwordChangeNotAllowedInTest")
                return false
            }
        }
        return true
    }

    private oldPasswordIsCorrect(PasswordCommand passwordCommand) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(passwordCommand.user.username, passwordCommand.currentPassword))
            return true
        } catch (AuthenticationException e) {
            passwordCommand.errors.rejectValue('currentPassword', 'passwordCommand.currentPassword.mismatch')
            return false
        }

    }
}
