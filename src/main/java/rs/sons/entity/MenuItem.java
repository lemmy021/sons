package rs.sons.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MenuItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long menu_item_id;
	
	@Column
	private int menu_item_position;
	
	@Column(nullable = false)
	private String menu_item_name;
	
	@Column(nullable = false)
	private String menu_item_url;
	
	@Transient
	private boolean menu_item_css_active = false;
	
	@ManyToOne
	@JoinColumn(name = "menu_item_menu_id")
	private Menu menu;
}
