package org.opentele.server.util

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails


class CaseInsensitivePasswordAuthenticationProvider extends DaoAuthenticationProvider {
    /**
     * This authenticator will first check password case insensitive, but will fall back to case sensitive check
     * if case insensitive fails. The passwords in the Database should be hashed lowercase, but this will allow
     * for old (case sensitive) passwords to be validatable.
     *
     * @param userDetails
     * @param authentication
     * @throws AuthenticationException
     */
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        Object salt = null

        if (this.saltSource != null) {
            salt = this.saltSource.getSalt(userDetails)
        }

        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided")

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"),
                    includeDetailsObject ? userDetails : null)
        }

        String presentedPassword = authentication.getCredentials().toString()

        if (!(isPasswordValid(userDetails, presentedPassword.toLowerCase(), salt) || isPasswordValid(userDetails, presentedPassword, salt))) {
            logger.debug("Authentication failed: password does not match stored value")

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"),
                    includeDetailsObject ? userDetails : null)
        }
    }

    def boolean isPasswordValid(UserDetails userDetails, String presentedPassword, salt) {
        passwordEncoder.isPasswordValid(userDetails.getPassword(), presentedPassword, salt)
    }

}
