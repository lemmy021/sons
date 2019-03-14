package rs.sons.dao;

import java.util.List;

import rs.sons.entity.User;

public interface UserDao {
	
	public User getUserById(int id);
	
	public User findUserByUsername(String username);
	
	public void saveUser(User user);
	
	public List<User> findAllUsers();
	
	public User findUserByEmail(String email);
	
	public User findUserByEmailForEdit(Integer userId, String email);
	
	public void updateUser(User user);
	
	public void deleteUserById(int userId);
}
