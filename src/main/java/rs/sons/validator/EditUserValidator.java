package rs.sons.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import rs.sons.entity.User;
import rs.sons.service.UserService;

@Component
public class EditUserValidator implements Validator{
	
	@Autowired
	UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		User user = (User) target;
		
		if (userService.findUserByEmailForEdit(user.getUser_id(), user.getUser_email()) != null) {
			errors.rejectValue("user_email", null, "Email address already exists !!!");
		}
		
	}

}
