package rs.sons.dao;

import java.util.List;

import rs.sons.entity.Client;

public interface ClientDao {

	public List<Client> findAllClients();
	
	public void saveClient(Client client);
	
	public Client findClientByEmail(String email);
	
	public Client findClientByPIB(String pib);
	
	public Client findClientByIdentificationNumber(String identification_number);
	
	public Client findClientById(int id);
	
	public void deleteClientById(int clientId);
	
	public void updateClient(Client client);
	
	public Client findClientByEmailForEdit(int clientId, String email);
	
	public Client findClientByPIBForEdit(int clientId, String pib);
	
	public Client findClientByIdentificationNumberForEdit(int clientId, String identification_number);
}
