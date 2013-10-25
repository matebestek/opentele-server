package org.opentele.server

import org.opentele.server.model.Permission
import org.opentele.server.model.Role
import org.opentele.server.model.RolePermission

class RoleService {

    def updatePermissions(Role role, String permissionId) {
        updatePermissions(role, [permissionId] as String[])
    }
    def updatePermissions(Role role, String[] permissionIds) {
        removePermissions(role)
        permissionIds.each {
            new RolePermission(role:role, permission:Permission.findById(it as Long)).save()
        }
    }

    def removePermissions(Role role) {
        RolePermission.removeAll(role)
    }
}
