package rs.sons.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.sons.dao.ClientDao;
import rs.sons.entity.Client;

@Service("clientService")
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	ClientDao clientDao;

	public List<Client> findAllClients() {
		return clientDao.findAllClients();
	}

	public void saveClient(Client client) {
		clientDao.saveClient(client);
	}

	public Client findClientByEmail(String email) {
		return clientDao.findClientByEmail(email);
	}

	public Client findClientByPIB(String pib) {
		return clientDao.findClientByPIB(pib);
	}

	public Client findClientByIdentificationNumber(String identification_number) {
		return clientDao.findClientByIdentificationNumber(identification_number);
	}

	public Client findClientById(int id) {
		return clientDao.findClientById(id);
	}

	public void deleteClientById(int clientId) {
		clientDao.deleteClientById(clientId);
	}

	public void updateClient(Client client) {
		clientDao.updateClient(client);
	}

	public Client findClientByEmailForEdit(int clientId, String email) {
		return clientDao.findClientByEmailForEdit(clientId, email);
	}

	public Client findClientByPIBForEdit(int clientId, String pib) {
		return clientDao.findClientByPIBForEdit(clientId, pib);
	}

	public Client findClientByIdentificationNumberForEdit(int clientId, String identification_number) {
		return clientDao.findClientByIdentificationNumberForEdit(clientId, identification_number);
	}

}
