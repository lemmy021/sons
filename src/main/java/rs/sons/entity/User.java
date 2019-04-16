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

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Invoice> invoicesCreated;
	
	@JsonIgnore
	@OneToMany(mappedBy = "userApplied")
	private List<Invoice> invoicesApplied;

	  //https://en.wikibooks.org/wiki/Java_Persistence/Relationships#Object_corruption.2C_one_side_of_the_relationship_is_not_updated_after_updating_the_other_side
	/*
	 * public void setInvoicesCreated(Invoice invoice) {
	 * this.invoicesCreated.add(invoice); if(invoice.getUser() != this) {
	 * invoice.setUser(this); } }
	 */
	 
	
	
}
