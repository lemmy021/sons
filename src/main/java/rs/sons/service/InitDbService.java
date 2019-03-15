package rs.sons.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.sons.dao.ClientDao;
import rs.sons.dao.GenderDao;
import rs.sons.dao.RoleDao;
import rs.sons.dao.UserDao;
import rs.sons.entity.Client;
import rs.sons.entity.Country;
import rs.sons.entity.Gender;
import rs.sons.entity.Role;
import rs.sons.entity.User;
import rs.sons.helper.PasswordEncoder;

@Transactional
@Service
public class InitDbService {
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	GenderDao genderDao;
	
	@Autowired
	ClientDao clientDao;
	
	//@PostConstruct
	private void init() {
		Role role = new Role();
		role.setRole_name("ROLE_ADMIN");
		roleDao.saveRole(role);
		
		Gender gender1 = new Gender();
		Gender gender2 = new Gender();
		
		gender1.setGender_name("Male");
		gender2.setGender_name("Female");
		
		genderDao.saveGender(gender1);
		genderDao.saveGender(gender2);
		
		
		
		User user = new User();
		
		user.setRole(role);
		user.setUser_address_city("Novi Sad");
		user.setUser_address_street("Turgenjeva 9");
		user.setUser_email("mile@test.com");
		user.setUser_enabled(true);
		user.setUser_name("Mile");
		
		//BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setUser_password(PasswordEncoder.encodePassword("123456"));
		
		user.setUser_phone("0658885115");
		user.setUser_surname("Kosovac");
		user.setUser_username("proba");
		
		user.setGender(gender1);
		
		userDao.saveUser(user);
		
		Country country = new Country();
		country.setCountry_id(1);
		country.setCountry_name("Srbija");
		
		Client client = new Client();
		
		client.setClient_name("Petar");
		client.setClient_surname("Petrovic");
		client.setClient_phone("54321");
		client.setClient_company("Cavalier doo");
		client.setClient_pib("22222");
		client.setClient_identification_number("333333");
		client.setClient_web_site("www.neobee.net");
		client.setClient_email("company@first.com");
		client.setClient_address_street("Turgenjeva 9");
		client.setClient_address_city("Novi Sad");
		client.setClient_address_zip("21000");
		//client.setClientType(true);
		client.setClient_type(true);
		client.setCountry(country);
		
		clientDao.saveClient(client);
		
	}

}
