package rs.sons.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import rs.sons.entity.User;

@Transactional
@Repository(value = "userDao")
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	EntityManager em;

	@SuppressWarnings("unchecked")
	public User getUserById(int id) {
		User user = null;

		Query query = em.createQuery("FROM User WHERE user_id = :id");
		query.setParameter("id", id);

		//user = (User) query.getSingleResult();
		user = (User) query.getResultList().stream().findFirst().orElse(null);

		return user;
	}

	@SuppressWarnings("unchecked")
	public User findUserByUsername(String username) {
		User user = null;

		Query query = em.createQuery("FROM User WHERE user_username = :username AND user_deleted = 0");
		query.setParameter("username", username);

		user = (User) query.getResultList().stream().findFirst().orElse(null);

		return user;
	}

	public void saveUser(User user) {
		em.persist(user);
	}

	public List<User> findAllUsers() {

			TypedQuery<User> query = em.createQuery(" FROM User WHERE user_deleted = 0", User.class);

			return query.getResultList();
		//return new ArrayList<>();
		
	}

	@SuppressWarnings("unchecked")
	public User findUserByEmail(String email) {
		User user = null;

		Query query = em.createQuery("FROM User WHERE user_email = :email AND user_deleted = 0");
		query.setParameter("email", email);

		user = (User) query.getResultList().stream().findFirst().orElse(null);

		return user;
	}

	@SuppressWarnings("unchecked")
	public User findUserByEmailForEdit(Integer userId, String email) {
		User user = null;

		Query query = em.createQuery("FROM User WHERE user_email = :email AND user_deleted = 0 AND user_id != :user_id");
		query.setParameter("email", email);
		query.setParameter("user_id", userId);

		user = (User) query.getResultList().stream().findFirst().orElse(null);

		return user;
	}

	public void updateUser(User user) {
		em.merge(user);
	}

	public void deleteUserById(int userId) {
		User user = em.find(User.class, userId);
		
		if(user != null) {
			user.setUser_deleted(true);
			em.merge(user);
		}
	}

}
