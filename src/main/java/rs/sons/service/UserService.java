package rs.sons.service;

import java.util.List;

import rs.sons.entity.User;

public interface UserService {

	public User getUserById(int id);
	
	public User findUserByUsername(String username);
	
	public List<User> findAllUsers();
	
	public void saveUser(User user);

	public User findUserByEmail(String email);
	
	public User findUserByEmailForEdit(Integer userId, String email);
	
	public void updateUser(User user);
	
	public void deleteUserById(int userId);
}
