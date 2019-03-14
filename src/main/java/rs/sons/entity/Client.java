package rs.sons.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@NamedQuery(name = "Client.findAllClients", query = "FROM Client c WHERE client_deleted = 0")
public class Client {

	// https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-enum-type-mapping-example/
	// https://vladmihalcea.com/the-best-way-to-map-an-enum-type-with-jpa-and-hibernate/

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer client_id;

	@NotNull
	@Pattern(regexp = "^[a-zA-Z0-9. ]{2,}$", message = "Please provide client name - only letters and digits, 2 characters minimum")
	private String client_name;

	@NotNull
	@Pattern(regexp = "^[a-zA-Z0-9. ]{2,}$", message = "Please provide client lastname - only letters and digits, 2 characters minimum")
	private String client_surname;

	@NotNull
	@Pattern(regexp = "\\d{5,}", message = "Only digits, 5 digits minimum")
	private String client_phone;

	@Pattern(regexp = "^[a-zA-Z0-9/. ]{2,}$", message = "Please provide company name - only letters and digits, 2 characters minimum")
	private String client_company = "n/a";

	@Pattern(regexp = "^[a-zA-Z0-9.]{1,}$", message = "Please provide company PIB - only letters and digits, 1 characters minimum")
	private String client_pib = "0";

	@Pattern(regexp = "^[a-zA-Z0-9.]{1,}$", message = "Please provide company identification number - only letters and digits, 1 characters minimum")
	private String client_identification_number = "0";

	private String client_web_site;

	@NotNull
	@Email(message = "Please provide a valid email")
	@NotEmpty(message = "Please provide an email")
	private String client_email;

	@NotNull
	@NotEmpty(message = "*Please provide street address")
	private String client_address_street;

	@NotNull
	@NotEmpty(message = "*Please provide city address")
	private String client_address_city;

	@NotNull
	@NotEmpty(message = "*Please provide ZIP code")
	private String client_address_zip;

	@NotNull
	// @Type(type="yes_no")
	private boolean client_type;

	private boolean client_deleted = false;
	
	@Transient
	private String client_jwt_helper;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "client_country")
	private Country country;

	@OneToMany(mappedBy = "client")
	private List<Invoice> invoices;
}
