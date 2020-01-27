package rs.sons.dao;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import rs.sons.entity.Menu;
import rs.sons.entity.MenuItem;

@Transactional
@Repository(value = "menuDao")
public class MenuDaoImpl implements MenuDao {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Menu> getLeftMenuItems() {

		TypedQuery<Menu> query = em.createQuery(" FROM Menu ORDER BY menu_position", Menu.class);

		return query.getResultList();
	}

	public boolean UrlExistsInMenuItemTable(String url) {
		
		//Query query = em.createQuery("SELECT CASE WHEN COUNT(mi)> 0 THEN true ELSE false END FROM MenuItem mi WHERE menu_item_url = :url");
		Query query = em.createQuery("FROM MenuItem WHERE menu_item_url = :url");
		query.setParameter("url", url);
		
		if(Objects.nonNull(query.getResultList().stream().findFirst().orElse(null))) {
			return true;
		} else {
			return false;
		}
		
	}

}
