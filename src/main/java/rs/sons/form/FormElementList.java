package rs.sons.form;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

//https://vkuzel.com/add-or-remove-items-from-a-list-of-objects-in-a-model-attribute-using-spring-mvc-and-thymeleaf

@Getter
@Setter
public class FormElementList {
	
	private String jwt;
	
	private List<Service> elementList;

}