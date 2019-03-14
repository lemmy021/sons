package rs.sons.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import rs.sons.entity.Country;

@Transactional
@Repository(value="countryDao")
public class CountryDaoImpl implements CountryDao {
	
	@PersistenceContext
	EntityManager em;

	public List<Country> getAllCountries() {
		
		TypedQuery<Country> query = em.createQuery("FROM Country ORDER BY country_name", Country.class);
		
		return query.getResultList();
	}

}
