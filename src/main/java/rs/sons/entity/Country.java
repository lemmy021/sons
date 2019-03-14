package rs.sons.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer country_id;

	@NotNull
	private String country_name;

	@NotNull
	private String country_code;

	// treba zbog smrdljivog tymeleaf-a
	public boolean isSelected(Integer id) {

		if (id != null) {
			return id.equals(country_id);
		}

		return false;
	}
}
