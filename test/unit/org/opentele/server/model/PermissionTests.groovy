package org.opentele.server.model

import grails.test.mixin.*
import grails.test.mixin.support.*

@TestMixin(GrailsUnitTestMixin)
class PermissionTests {
    void testFindsActualNameByRemovingIrrelevantPrefix() {
        assert (new Permission(permission: 'ROLE_MY_ID').actualName) == 'MY_ID'
    }

    void testStripsSpaceFromActualName() {
        assert (new Permission(permission: '  ROLE_MY_ID  ').actualName) == 'MY_ID'
    }
}
