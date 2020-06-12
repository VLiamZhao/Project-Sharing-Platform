package com.psp.service;

import com.psp.model.Role;
import com.psp.repository.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleDao roleDao;

    public Role getRoleByName(String roleName){
        return roleDao.getRoleByName(roleName);
    }

    public Role save(Role role){
        return roleDao.save(role);
    }

    public boolean deleteRoleById(long id){
        return roleDao.deleteRoleById(id);
    }

    public Role getRoleById(long id){
        return roleDao.getRoleById(id);
    }

    public List<Role> getAllRoles(){
        return roleDao.getRoles();
    }

    public Role updateRole(Role role){
        return roleDao.updateRole(role);
    }
}
