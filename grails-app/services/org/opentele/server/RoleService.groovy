package org.opentele.server

import org.opentele.server.model.Permission
import org.opentele.server.model.Role
import org.opentele.server.model.RolePermission

class RoleService {
    static transactional = false

    boolean updateRole(Role role, String authority, String[] permissionIds) {
        Role.withTransaction { status ->
            role.authority = authority

            if (!role.save(flush: true)) {
                status.setRollbackOnly()
                return false
            }

            RolePermission.removeAll(role)
            permissionIds.each {
                boolean rolePermissionSaved = new RolePermission(role:role, permission:Permission.findById(it as Long)).save()
                if (!rolePermissionSaved) {
                    status.setRollbackOnly()
                    return false
                }
            }

            return true
        }
    }
}
