package rs.sons.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.sons.dao.RoleDao;
import rs.sons.entity.Role;

@Service("roleService")
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleDao roleDao;

	@Override
	public void saveRole(Role role) {
		
		roleDao.saveRole(role);
	}

	@Override
	public List<Role> getAllRoles() {
		return roleDao.getAllRoles();
	}

}
