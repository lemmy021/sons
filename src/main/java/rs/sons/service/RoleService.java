package rs.sons.service;

import java.util.List;

import rs.sons.entity.Role;

public interface RoleService {

	public void saveRole(Role role);
	
	public List<Role> getAllRoles();
}
