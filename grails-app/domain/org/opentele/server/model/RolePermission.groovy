package org.opentele.server.model

import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * Maps roles and permissions
 */
class RolePermission extends AbstractObject {

    Role role
    Permission permission

    static constraints = {
        role(nullable: false)
        permission(nullable: false)
    }

    boolean equals(other) {
        if (!(other instanceof RolePermission)) {
            return false
        }

        other.role?.id == role?.id &&
                other.permission?.id == permission?.id
    }


    String toString() {
       return "Role: " + role.authority + " permission: " + permission.permission
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (role) builder.append(role.id)
        if (permission) builder.append(permission.id)
        builder.toHashCode()
    }

    static RolePermission get(long roleId, long permissionId) {
        find 'from RolePermission where role.id=:roleId and permission.id=:permissionId',
                [roleId: roleId, permissionId: permissionId]
    }

    static RolePermission create(Role role, Permission permission, boolean flush = false) {
        new RolePermission(role: role, permission: permission).save(flush: flush, insert: true)
    }

    static boolean remove(Role role, Permission permission, boolean flush = false) {
        RolePermission instance = RolePermission.findByRoleAndPermission(role, permission)
        if (!instance) {
            return false
        }

        instance.delete(flush: flush)
        true
    }

    static void removeAll(Role role) {
        executeUpdate 'DELETE FROM RolePermission WHERE role=:role', [role: role]
    }

    static void removeAll(Permission permission) {
        executeUpdate 'DELETE FROM RolePermission WHERE permission=:permission', [permission: permission]
    }

    static mapping = {
        id composite: ['permission','role']
        version false
    }


}
