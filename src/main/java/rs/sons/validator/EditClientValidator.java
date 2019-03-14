package rs.sons.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import rs.sons.entity.Client;
import rs.sons.service.ClientService;

@Component
public class EditClientValidator implements Validator {
	
	@Autowired
	ClientService clientService;

	public boolean supports(Class<?> clazz) {
		return Client.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {

		Client client = (Client) target;
		
		if(clientService.findClientByEmailForEdit(client.getClient_id(), client.getClient_email()) != null) {
			errors.rejectValue("client_email", null, "Email address already exists !!!");
		}
		
		if(clientService.findClientByIdentificationNumberForEdit(client.getClient_id(), client.getClient_identification_number()) != null) {
			errors.rejectValue("client_identification_number", null, "Identification number already exists !!!");
		}
		
		if(clientService.findClientByIdentificationNumberForEdit(client.getClient_id(), client.getClient_pib()) != null) {
			errors.rejectValue("client_pib", null, "PIB already exists !!!");
		}
	}

}
