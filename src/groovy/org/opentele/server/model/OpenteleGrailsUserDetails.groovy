package org.opentele.server.model

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
import org.springframework.security.core.GrantedAuthority

class OpenteleGrailsUserDetails extends GrailsUser {
    /**
     *  Constructor.
     *
     * @param username the username presented to the
     *         <code>DaoAuthenticationProvider</code>
     * @param password the password that should be presented to the
     *         <code>DaoAuthenticationProvider</code>
     * @param enabled set to <code>true</code> if the user is enabled
     * @param accountNonExpired set to <code>true</code> if the account has not expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials have not expired
     * @param accountNonLocked set to <code>true</code> if the account is not locked
     * @param authorities the authorities that should be granted to the caller if they
     *         presented the correct username and password and the user is enabled. Not null.
     * @param id the id of the domain class instance used to populate this
     */
    OpenteleGrailsUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities, Object id) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id)
    }
}
