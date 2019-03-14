package rs.sons.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer permission_id;
	
	@NotNull
	private String permission_action;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "permission_role_id")
	private Role role;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "permission_resource_id")
	private Resource resource; 
}
