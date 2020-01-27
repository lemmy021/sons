package rs.sons.controller;

//https://www.boraji.com/spring-mvc-4-sessionattributes-example

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rs.sons.entity.Client;
import rs.sons.helper.CapitalizeClientFields;
import rs.sons.jwt.JwtHelper;
import rs.sons.service.ClientService;
import rs.sons.service.CountryService;
import rs.sons.validator.EditClientValidator;
import rs.sons.validator.NewClientValidator;

@Controller
@SessionAttributes("clientsession")
public class ClientController {

	@Autowired
	NewClientValidator newClientValidator;
	
	@Autowired
	EditClientValidator editClientValidator;
	
	@InitBinder("clientforedit")
	protected void initBinderEditClient(WebDataBinder binder) {
		binder.addValidators(editClientValidator);
	}

	@InitBinder("addclientmodel")
	protected void initBinderAddClient(WebDataBinder binder) {
		binder.addValidators(newClientValidator);
	}

	@Autowired
	ClientService clientService;

	@Autowired
	CountryService countryService;

	@ModelAttribute("addclientmodel")
	public Client constructClient() {
		return new Client();
	}

	@ModelAttribute("clientsession")
	public Client clientForSession() {
		return new Client();
	}

	@ModelAttribute
	public void retrieveAllCountries(Model model) {
		model.addAttribute("countries", countryService.getAllCountries());
	}

	@GetMapping("/clients")
	public String showAllClients(Model model) {
		model.addAttribute("clients", clientService.findAllClients());

		return "clients";
	}

	@GetMapping("/addclient")
	public String addClientForm(Model model) {
		// treba zbog smrdljivog tymeleaf-a, 189 je ID za srbiju u bazi
		model.addAttribute("lastselectedcountry", 189);
		return "addclientform";
	}

	@PostMapping("/addclient")
	public String saveClient(@Valid @ModelAttribute("addclientmodel") Client client, BindingResult result, RedirectAttributes redirectAttributes, Model model) {

		if (result.hasErrors()) {
			// treba zbog smrdljivog tymeleaf-a
			model.addAttribute("lastselectedcountry", client.getCountry().getCountry_id());
			return "addclientform";
		}

		CapitalizeClientFields capitalizeClientFields = new CapitalizeClientFields(client);

		clientService.saveClient(capitalizeClientFields.getClient());

		redirectAttributes.addFlashAttribute("success", true);

		return "redirect:/addclient";
	}

	@GetMapping(value = { "/client/{jwtId:.+}", "/client" })
	public String showClientData(@ModelAttribute("clientsession") Client client, Model model, @PathVariable("jwtId") String jwtId, RedirectAttributes redirectAttributes) {

		Integer clientId = JwtHelper.decodeJWT(jwtId);

		/*
		 * jwt string is ok so we can move on
		 */
		if (clientId > 0) {
			Client clientDb = clientService.findClientById(clientId);

			if (clientDb != null) {
				client = clientDb;
				model.addAttribute("client", clientDb);
			} else {
				redirectAttributes.addFlashAttribute("noclient", true);
				return "redirect:/clients";
			}
		} else {
			redirectAttributes.addFlashAttribute("noclient", true);
			return "redirect:/clients";
		}

		return "clientcard";
	}

	@PostMapping("/deleteclient")
	@ResponseBody
	public String ajaxRemoveClient(@RequestParam("jwt_client_id") String jwtClientId) {
		Integer clientId = JwtHelper.decodeJWT(jwtClientId);

		if (clientId > 0) {
			clientService.deleteClientById(clientId);

			return "1";
		}

		return "redirect:/clients";
	}

	@GetMapping(value = { "/editclient/{jwtId:.+}", "/editclient" })
	public String editClientForm(Model model, @PathVariable("jwtId") String jwtId, RedirectAttributes redirectAttributes) {

		Integer clientId = JwtHelper.decodeJWT(jwtId);
		/*
		 * jwt string is ok so we can move on
		 */
		if (clientId > 0) {
			Client clientDb = clientService.findClientById(clientId);

			if (clientDb != null) {
				clientDb.setClient_jwt_helper(jwtId);
				model.addAttribute("clientforedit", clientDb);

				// mora zbog thymeleaf-a
				model.addAttribute("lastselectedcountry", clientDb.getCountry().getCountry_id());
			} else {
				redirectAttributes.addFlashAttribute("noclient", true);
				return "redirect:/clients";
			}
		} else {
			redirectAttributes.addFlashAttribute("noclient", true);
			return "redirect:/clients";
		}

		return "editclientform";
	}

	@PostMapping("/editclient")
	public String saveEditedClientData(Model model, @ModelAttribute("clientforedit") @Validated Client client, BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			// treba zbog smrdljivog tymeleaf-a
			model.addAttribute("lastselectedcountry", client.getCountry().getCountry_id());
			return "editclientform";
		}

		clientService.updateClient(client);
		redirectAttributes.addFlashAttribute("clientedited", true);

		return "redirect:/client/" + client.getClient_jwt_helper();
	}
}
