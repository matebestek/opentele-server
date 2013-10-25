package org.opentele.server

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.opentele.server.model.OpenteleGrailsUserDetails
import org.opentele.server.model.RolePermission
import org.opentele.server.model.User
import org.opentele.server.model.UserRole
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * Implement UserDetails service to handle roles and permissions
 */
class UserDetailsService implements GrailsUserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class.name)
    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]

    @Override
    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException, DataAccessException {
        return loadUserByUsername(username)
    }

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException, DataAccessException {
        User.withTransaction { status ->
            User user = User.findByUsername(s)
            if (!user) {
                throw new UsernameNotFoundException('User not found', s)
            }

            def authorities = []
            user.authorities.each {
                def perms = RolePermission.findAllByRole(it)
                perms.each { perm ->
                    authorities << new GrantedAuthorityImpl(perm.permission.permission)
                }
            }

            return new OpenteleGrailsUserDetails (user.username, user.password, user.enabled,
                    !user.accountExpired, !user.passwordExpired,
                    !user.accountLocked, authorities ?: NO_ROLES, user.id)
        }
    }
}
