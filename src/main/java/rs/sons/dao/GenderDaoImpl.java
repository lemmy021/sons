package rs.sons.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import rs.sons.entity.Gender;

@Transactional
@Repository(value = "genderDao")
public class GenderDaoImpl implements GenderDao {

	@PersistenceContext
	EntityManager em;
	
	public void saveGender(Gender gender) {
		em.persist(gender);
	}

}
