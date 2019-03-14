package rs.sons.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import rs.sons.entity.Role;

@Transactional
@Repository(value = "roleDao")
public class RoleDaoImpl implements RoleDao {

	@PersistenceContext
	EntityManager em;
	
	public void saveRole(Role role) {
		em.persist(role);
	}

	@Override
	public List<Role> getAllRoles() {
		
		TypedQuery<Role> query = em.createQuery("FROM Role", Role.class);
		
		return query.getResultList();
	}

}
