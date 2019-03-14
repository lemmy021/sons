package rs.sons.helper;

import org.springframework.util.StringUtils;

import rs.sons.entity.Client;

public class CapitalizeClientFields {
	
	private Client client;

	public CapitalizeClientFields(Client client) {
		super();
		this.client = client;
		fixLetters();
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	private void fixLetters() {
		client.setClient_name(setFirstLetterCapitalized(client.getClient_name().toLowerCase()));
		client.setClient_surname(setFirstLetterCapitalized(client.getClient_surname().toLowerCase()));
		client.setClient_address_city(setFirstLetterCapitalized(client.getClient_address_city().toLowerCase()));
		client.setClient_address_street(setFirstLetterCapitalized(client.getClient_address_street().toLowerCase()));
		client.setClient_email(client.getClient_email().toLowerCase());
	}
	
	private String setFirstLetterCapitalized(String field) {
		
		String[] splitedWords = field.split(" ");
		
		if(splitedWords.length > 1) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < splitedWords.length; i++) {
				sb.append(StringUtils.capitalize(splitedWords[i]) + " ");
			}
			
			return sb.substring(0, sb.length() - 1);
		} else {
			return StringUtils.capitalize(field);
		}
	}
	

}
