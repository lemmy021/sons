package rs.sons.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rs.sons.entity.Role;
import rs.sons.entity.User;
import rs.sons.helper.PasswordEncoder;
import rs.sons.jwt.JwtHelper;
import rs.sons.service.RoleService;
import rs.sons.service.UserService;
import rs.sons.validator.EditUserValidator;
import rs.sons.validator.NewUserValidator;

@Controller
public class IndexController extends HelperController {
	
	@Autowired
	private NewUserValidator newUserValidator;
	
	@Autowired
	private EditUserValidator editUserValidator;
	
	@InitBinder("adduser")
	protected void initBinderAddUser(WebDataBinder binder) {
		binder.addValidators(newUserValidator);
	}
	
	@InitBinder
	protected void initBinderEditUser(WebDataBinder binder) {
		binder.addValidators(editUserValidator);
	}
	
	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;
	
	@ModelAttribute
    public void showAllRoles(Model model){
        model.addAttribute("roles", this.getAllRoles());
    }
	
	@ModelAttribute("adduser")
	public User constructUser() {
		return new User();
	}
	
	@ModelAttribute("allGenders")
	public Map<Integer, String> populateRoles()
	{
		Map<Integer, String> genders = new HashMap<>();
		genders.put(1, "Male");
		genders.put(2,  "Female");
	    
	    return genders;
	}
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello(Model model) {
		model.addAttribute("name", "Hello Lemmy");
		return "hello";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:/";
		} else {
			return "login";
		}
	}

	@RequestMapping(value = { "/", "index" }, method = RequestMethod.GET)
	public String root(Model model, HttpSession session, Principal principal) {

		model.addAttribute("users", userService.findAllUsers());

		User loggedUser = userService.findUserByUsername(principal.getName());

		if(session.getAttribute("user_name_surname") == null) {
			setSessionVariables("user_name_surname", loggedUser.getUser_name() + " " + loggedUser.getUser_surname(),session);
			setSessionVariables("user_email", loggedUser.getUser_email(), session);
			setSessionVariables("user_gender_name", loggedUser.getGender().getGender_name(), session);
		}

		return "index";
	}

	@RequestMapping(value = "/adduser", method = RequestMethod.GET)
	public String addUserForm(/*User user, Model model*/) {
		
		return "adduserform";
	}
	
	@PostMapping(value = "/adduser")
	public String saveUser(@Valid @ModelAttribute("adduser") User user, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
		
		if(result.hasErrors()) {
			return "adduserform";
		}
		
		//set encoded password
		user.setUser_password(PasswordEncoder.encodePassword(user.getUser_password()));
		
		userService.saveUser(user);
		redirectAttributes.addFlashAttribute("success", true);
		
		return "redirect:/adduser";
	}
	
	/*
	 * resenje za tranketovanje zadnje tacke
	 * https://www.mkyong.com/spring-mvc/spring-mvc-pathvariable-dot-get-truncated/
	 */
	@GetMapping(value = {"/edituser/{jwtId:.+}", "/edituser"})
	public String editUserForm(User user, Model model, @PathVariable("jwtId") String jwtId, RedirectAttributes redirectAttributes) {
		
		Integer userId = JwtHelper.decodeJWT(jwtId);
		/*
		 * jwt string is ok so we can move on
		 */
		if(userId > 0) {
			
			User dbUser = userService.getUserById(userId);
			
			if(dbUser != null) {
				dbUser.setUser_jwt_helper(jwtId);
				
				model.addAttribute("user", dbUser);
			} else {
				redirectAttributes.addFlashAttribute("nouserforedit", true);
				return "redirect:/";
			}
		} else {
			redirectAttributes.addFlashAttribute("nouserforedit", true);
			return "redirect:/";
		}
		
		return "edituserform";
	}
	
	@PostMapping("/edituser")
	public String saveEditedUserData(@ModelAttribute("user") @Validated User user, BindingResult result, RedirectAttributes redirectAttributes) {
		
		if(result.hasErrors()) {
			return "edituserform";
		}
		
		user.setUser_password(PasswordEncoder.encodePassword(user.getUser_password()));
		userService.updateUser(user);
		
		redirectAttributes.addFlashAttribute("success", true);
		
		return "redirect:/edituser/" + user.getUser_jwt_helper();
	}
	
	@PostMapping("/deleteuser")
	@ResponseBody
	public String removeUser(@RequestParam("jwt_user_id") String jwtUserId) {
		Integer userId = JwtHelper.decodeJWT(jwtUserId);
		
		if(userId > 0) {
			userService.deleteUserById(userId);
			
			return "1";
		}
		
		return "redirect:/";
	}
	
	@PostMapping(value = "/userdetails", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User userDetails(@RequestParam("jwt_user_id") String jwtUserId) {
		
		//removing prefix "info_"
		jwtUserId = jwtUserId.substring(5);
		
		Integer userId = JwtHelper.decodeJWT(jwtUserId);
		
		return userService.getUserById(userId);
	}
	
	private Map<Integer, String> getAllRoles() {
		Map<Integer, String> mapOfRoles = new HashMap<Integer, String>();
		List<Role> listOfRoles = roleService.getAllRoles();
		
		for(Role role : listOfRoles) {
			mapOfRoles.put(role.getRole_id(), role.getRole_name());
		}
		
		return mapOfRoles;
	}

}
