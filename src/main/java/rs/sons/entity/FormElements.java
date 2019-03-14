package rs.sons.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//http://outbottle.com/spring-3-mvc-adding-objects-to-a-list-element-on-the-fly-at-form-submit-generic-method/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormElements {
	
	private String service;
	
	private String amount;
	
	private String unit;
	
	private String price;

}
