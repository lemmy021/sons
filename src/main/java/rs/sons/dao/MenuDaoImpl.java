package rs.sons.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import rs.sons.entity.Menu;

@Transactional
@Repository(value = "menuDao")
public class MenuDaoImpl implements MenuDao {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Menu> getLeftMenuItems() {

		TypedQuery<Menu> query = em.createQuery(" FROM Menu", Menu.class);

		return query.getResultList();
	}

}
