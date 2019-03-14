package rs.sons.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import rs.sons.service.UserService;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	/*
	 * prvi parametar pokazuje da ce se raditi sa UniqueUsername anotacijom drugi
	 * parametar - tip podataka sa kojima ce se raditi
	 */

	@Autowired
	UserService userService;

	public void initialize(UniqueUsername xx) {
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {

		/*
		 * ovaj if se koristi zbog initDBService-e gde inicijalno punimo bazu posto je u
		 * tom trenutku userRepository null bez toga nam ovaj if ne treba
		 */
		if (userService == null) {
			return true;
		}

		// ako je null vratice true;
		return userService.findUserByUsername(username) == null;
	}

}
