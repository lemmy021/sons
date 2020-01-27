package rs.sons.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OrderBy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer menu_id;
	
	@NotNull
	private String menu_name;
	
	@NotNull
	private int menu_position;
	
	@NotNull
	private String menu_icon;
	
	@Transient
	private boolean menu_css_active = false;
	
	//https://www.baeldung.com/hibernate-sort
	@OrderBy(clause = "menu_item_position ASC")
	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<MenuItem> menuItems;
	
	public void addMenuItem(MenuItem menuItem) {
		menuItems.add(menuItem);
		menuItem.setMenu(this);
	}
}
