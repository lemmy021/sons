package rs.sons.dao;

import java.util.List;

import rs.sons.entity.Role;

public interface RoleDao {

	public void saveRole(Role role);
	
	public List<Role> getAllRoles();
}
