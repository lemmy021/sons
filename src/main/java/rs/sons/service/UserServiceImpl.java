package rs.sons.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.sons.dao.UserDao;
import rs.sons.entity.User;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	public User getUserById(int id) {
		return userDao.getUserById(id);
	}
	
	public User findUserByUsername(String username) {
		return userDao.findUserByUsername(username);
	}

	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

	public void saveUser(User user) {
		userDao.saveUser(user);
	}

	public User findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}

	public User findUserByEmailForEdit(Integer userId, String email) {
		return userDao.findUserByEmailForEdit(userId, email);
	}
	
	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	public void deleteUserById(int userId) {
		userDao.deleteUserById(userId);
	}

}
