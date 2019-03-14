package rs.sons.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import rs.sons.entity.User;
import rs.sons.service.UserService;

@Component
//http://java.candidjava.com/tutorial/spring-custom-validator-example-with-Validator-interface.htm
//oba validatora --> https://www.mkyong.com/spring-mvc/combine-spring-validator-and-hibernate-validator/
public class NewUserValidator implements Validator {

	@Autowired
	UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		User user = (User) target;

		if (userService.findUserByUsername(user.getUser_username()) != null) {
			errors.rejectValue("user_username", null, "Such username already exists !!!");
		}

		if (userService.findUserByEmail(user.getUser_email()) != null) {
			errors.rejectValue("user_email", null, "Email address already exists !!!");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender.gender_id", null, "Please select user gender");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role.role_id", null, "Please select user role");
		

	}
}
