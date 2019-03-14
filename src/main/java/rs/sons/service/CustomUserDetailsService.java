package rs.sons.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import rs.sons.entity.User;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//User user = userService.getUserById(1);
		User user = userService.findUserByUsername(username);
		
		if(user == null){
			throw new UsernameNotFoundException("Username not found");
		}
		
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
		
		return new org.springframework.security.core.userdetails.User(
				user.getUser_username(), 
				user.getUser_password(),
				user.isUser_enabled(),
				accountNonExpired,
				credentialsNonExpired,
				accountNonLocked,
				getGrantedAuthorities(user));
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(User user){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		authorities.add(new SimpleGrantedAuthority(user.getRole().getRole_name()));
		
		return authorities;
	}

}
