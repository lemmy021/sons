package rs.sons.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer user_id;
	
	//@UniqueUsername(message = "Such username already exists !!!")
	@Pattern(regexp = "^[a-zA-Z0-9]{5,}$", message = "Please enter your username - only letters and digits, 5 characters minimum")
	private String user_username;
	
	@NotNull
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String user_password;
	
	@Email(message = "Please provide a valid email")
	@NotEmpty(message = "Please provide an email")
	private String user_email;
	
	@Pattern(regexp = "^[a-zA-Z0-9]{2,}$", message = "Please provide name - only letters and digits, 2 characters minimum")
	private String user_name;
	
	@Pattern(regexp = "^[a-zA-Z0-9]{2,}$", message = "Please provide surname - only letters and digits, 2 characters minimum")
	private String user_surname;
	
	@NotEmpty(message = "*Please provide street address")
	private String user_address_street;
	
	@NotEmpty(message = "*Please provide city address")
	private String user_address_city;
	
	@Pattern(regexp = "\\d{5,}", message = "Only digits, 5 digits minimum")
	private String user_phone;
	
	private boolean user_enabled = true;
	
	private boolean user_deleted = false;
	
	@Transient
	private String user_jwt_helper;
	
	@ManyToOne
	@JoinColumn(name = "user_role_id")
	private Role role;
	
	@ManyToOne
	@JoinColumn(name = "user_gender_id")
	private Gender gender;
	
	@OneToMany(mappedBy = "user")
	private List<Invoice> invoicesCreated;
	
	@OneToMany(mappedBy = "userApplied")
	private List<Invoice> invoicesApplied;

	/*public User() {
		super();
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUser_username() {
		return user_username;
	}

	public void setUser_username(String user_username) {
		this.user_username = user_username;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_surname() {
		return user_surname;
	}

	public void setUser_surname(String user_surname) {
		this.user_surname = user_surname;
	}

	public String getUser_address_street() {
		return user_address_street;
	}

	public void setUser_address_street(String user_address_street) {
		this.user_address_street = user_address_street;
	}

	public String getUser_address_city() {
		return user_address_city;
	}

	public void setUser_address_city(String user_address_city) {
		this.user_address_city = user_address_city;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public boolean isUser_enabled() {
		return user_enabled;
	}

	public void setUser_enabled(boolean user_enabled) {
		this.user_enabled = user_enabled;
	}

	public boolean isUser_deleted() {
		return user_deleted;
	}

	public void setUser_deleted(boolean user_deleted) {
		this.user_deleted = user_deleted;
	}

	public String getUser_jwt_helper() {
		return user_jwt_helper;
	}

	public void setUser_jwt_helper(String user_jwt_helper) {
		this.user_jwt_helper = user_jwt_helper;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}*/

}
