package com.psp.repository;

import com.psp.model.Role;
import com.psp.model.User;

import java.util.List;

public interface RoleDao {
    Role getRoleByName(String name);
    Role getRoleById(long id);
    Role save(Role role);
    boolean deleteRoleById(long id);
    List<Role> getRoles();
    Role updateRole(Role role);
}
