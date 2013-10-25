package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import org.opentele.server.RoleService
import spock.lang.Ignore
import spock.lang.Specification

@TestFor(RoleController)
@Build([Role, RolePermission, User, UserRole])
class RoleControllerSpec extends Specification {
    def "test create when when no id"() {
        when:
        def model = controller.create()

        then:
        !model.permissions
        model.roleInstance
        !model.roleInstance.id
    }

    def "test create when doing copy of roles"() {
        setup:
        def role = Role.build()

        def permission1 = new Permission(permission: "A")
        RolePermission.build(role: role, permission: permission1)

        def permission2 = new Permission(permission: "B")
        RolePermission.build(permission: permission2)

        when:
        params.id = role.id

        and:
        def model = controller.create()

        then:
        model.permissions == [permission1]
    }

    def "test create where role id does not exists"() {
        setup:
        RolePermission.build(role: Role.build(), permission: new Permission(permission: "A"))

        when:
        params.id = Role.build().id + 1

        and:
        def model = controller.create()

        then:
        !model.permissions
    }

    @Ignore
    def "test delete role"() {
        setup:
        Role role = Role.build(authority: authority)

        when:
        params.id = role.id

        and:
        controller.delete()

        then:
        (Role.count() == 0) == deleteSucceeds

        where:
        authority       | deleteSucceeds
        "Administrator" | true
        "Patient"       | false
    }

    def "test rename role"() {
        setup:
        def roleService = mockFor(RoleService)
        roleService.demand.removePermissions{ -> }
        controller.roleService = roleService.createMock()

        and:
        Role role = Role.build(authority: authority)

        when:
        params.id = role.id
        params.authority = "NewAuthority"
        params.permissions = []

        and:
        controller.update()

        then:
        (Role.findAll()[0].authority) == authorityAfterRename

        where:
        authority       | authorityAfterRename
        "Administrator" | "NewAuthority"
        "Patient"       | "Patient"
    }
}
