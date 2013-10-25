package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

@TestMixin(GrailsUnitTestMixin)
@Build([Role, RolePermission, Permission])
class BootStrapUtilTests {
    void testCanCreateQuestionnaire() {

    }

    void testCanCleanUpRole() {
        def role = Role.build()
        role.save(failOnError: true)
        createPermission(role, 'ROLE_PERMISSION1')
        createPermission(role, 'ROLE_PERMISSION2')

        assert Role.findAll().size() == 1
        assert RolePermission.findAll().size() == 2
        assert Permission.findAll().size() == 2

        BootStrapUtil.removePermission('ROLE_PERMISSION1')

        def roles = Role.findAll()
        def rolePermissions = RolePermission.findAll()
        def permissions = Permission.findAll()

        assert roles.size() == 1
        assert rolePermissions.size() == 1
        assert permissions.size() == 1

        assert rolePermissions[0].role == role
        assert rolePermissions[0].permission == permissions[0]
        assert permissions[0].permission == 'ROLE_PERMISSION2'
    }

    void testDoesNothingIfRemovingNonexistingPermission() {
        def role = Role.build()
        role.save(failOnError: true)
        createPermission(role, 'ROLE_PERMISSION1')
        createPermission(role, 'ROLE_PERMISSION2')

        assert Role.findAll().size() == 1
        assert RolePermission.findAll().size() == 2
        assert Permission.findAll().size() == 2

        BootStrapUtil.removePermission('ROLE_PERMISSION3')

        def roles = Role.findAll()
        def rolePermissions = RolePermission.findAll()
        def permissions = Permission.findAll()

        assert roles.size() == 1
        assert rolePermissions.size() == 2
        assert permissions.size() == 2
    }

    private void createPermission(role, permissionName) {
        def permission = Permission.build(permission: permissionName)
        permission.save(failOnError: true)
        def rolePermission = RolePermission.build(role: role, permission: permission)
        rolePermission.save(failOnError: true)
    }
}
