package rs.sons.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.google.common.base.Strings;

import rs.sons.service.UserService;

@Component
public abstract class HelperController {

	@Autowired
	UserService userService;
	
	@ModelAttribute
    public void getLeftMenuItems(Model model) throws IOException{
        model.addAttribute("leftmenuitems", "levi krose meni");
        model.addAttribute("about", userService.getUserById(1));
        //model.addAttribute("weather",weatherService.getNsWeather());

        //... add other attributes you need to show
    }
	
	public void setSessionVariables(String variableName, String variableValue, HttpSession session) {
		
		if(Strings.isNullOrEmpty((@Nullable String) session.getAttribute(variableName))) {
			session.setAttribute(variableName, variableValue);
		}
	}
	
	/*public String encodePassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(password);
	}*/
	
	/*public BindingResult extraCheckForError(BindingResult result, Optional<Integer> fieldId, String object, String field, String message) {
		
		if(!fieldId.isPresent()) {
			result.addError(new FieldError(object, field, message));
		}
		
		return result;
	}*/
}
