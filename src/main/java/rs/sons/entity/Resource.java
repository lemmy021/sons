package rs.sons.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer resource_id;
	
	@NotNull
	private String resource_controller;
	
	@NotNull
	private String resource_method;
	
	@OneToMany(mappedBy = "resource")
	private List<Permission> permissions;
	
	public void addPermission(Permission permission) {
		this.permissions.add(permission);
	}
}
