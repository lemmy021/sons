package rs.sons.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Test {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer test_id;

	@NotNull
	@Size(min = 5, message = "Name must be at least 5 characters !!!")
	private String test_name;

	@NotNull
	@Size(min = 6, message = "Phone must be at least 6 characters !!!")
	private String test_phone;

	public Test() {
		super();
	}

	public Test(Integer test_id, String test_name, String test_phone) {
		super();
		this.test_id = test_id;
		this.test_name = test_name;
		this.test_phone = test_phone;
	}

	public Integer getTest_id() {
		return test_id;
	}

	public void setTest_id(Integer test_id) {
		this.test_id = test_id;
	}

	public String getTest_name() {
		return test_name;
	}

	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}

	public String getTest_phone() {
		return test_phone;
	}

	public void setTest_phone(String test_phone) {
		this.test_phone = test_phone;
	}
}
