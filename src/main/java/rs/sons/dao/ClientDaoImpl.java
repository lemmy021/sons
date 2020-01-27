package rs.sons.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import rs.sons.entity.Client;

@Transactional
@Repository(value = "clientDao")
public class ClientDaoImpl implements ClientDao {
	
	@PersistenceContext
	EntityManager em;

	@SuppressWarnings("unchecked")
	@Cacheable("clients")
	public List<Client> findAllClients() {
		return em.createNamedQuery("Client.findAllClients").getResultList();
	}
	
	@CacheEvict(allEntries = true, cacheNames = { "clients" })
	@Scheduled(fixedDelay = 300000)
	public void cacheEvict() {
		System.out.println("odradio skedjuler");
	}

	public void saveClient(Client client) {
		em.persist(client);
	}

	@SuppressWarnings("unchecked")
	public Client findClientByEmail(String email) {
		
		Query query = em.createQuery("FROM Client WHERE client_email = :email AND client_deleted = 0");
		query.setParameter("email", email);

		return (Client) query.getResultList().stream().findFirst().orElse(null);
		
	}

	@SuppressWarnings("unchecked")
	public Client findClientByPIB(String pib) {
		Query query = em.createQuery("FROM Client WHERE client_pib = :pib AND client_deleted = 0 AND client_pib != '0'");
		query.setParameter("pib", pib);

		return (Client) query.getResultList().stream().findFirst().orElse(null);
	}

	@SuppressWarnings("unchecked")
	public Client findClientByIdentificationNumber(String identification_number) {
		Query query = em.createQuery("FROM Client WHERE client_identification_number = :identification_number AND client_deleted = 0 AND client_identification_number != '0'");
		query.setParameter("identification_number", identification_number);

		return (Client) query.getResultList().stream().findFirst().orElse(null);
	}

	@SuppressWarnings("unchecked")
	public Client findClientById(int id) {

		Query query = em.createQuery("FROM Client WHERE client_id = :id");
		query.setParameter("id", id);
		
		return (Client) query.getResultList().stream().findFirst().orElse(null);
	}

	public void deleteClientById(int clientId) {
		Client client = em.find(Client.class, clientId);
		
		if(client != null) {
			client.setClient_deleted(true);
			em.merge(client);
		}
	}

	public void updateClient(Client client) {
		em.merge(client);
	}

	@SuppressWarnings("unchecked")
	public Client findClientByEmailForEdit(int clientId, String email) {
		
		Query query = em.createQuery("FROM Client WHERE client_email = :email AND client_deleted = 0 AND client_id != :client_id");
		query.setParameter("email", email);
		query.setParameter("client_id", clientId);
		
		return (Client) query.getResultList().stream().findFirst().orElse(null);
	}

	@SuppressWarnings("unchecked")
	public Client findClientByPIBForEdit(int clientId, String pib) {
		
		Query query = em.createQuery("FROM Client WHERE client_pib = :pib AND client_deleted = 0 AND client_id != :client_id");
		query.setParameter("pib", pib);
		query.setParameter("client_id", clientId);
		
		return (Client) query.getResultList().stream().findFirst().orElse(null);
	}

	@SuppressWarnings("unchecked")
	public Client findClientByIdentificationNumberForEdit(int clientId, String identification_number) {
		
		Query query = em.createQuery("FROM Client WHERE client_identification_number = :client_identification_number AND client_deleted = 0 AND client_id != :client_id");
		query.setParameter("client_identification_number", identification_number);
		query.setParameter("client_id", clientId);
		
		return (Client) query.getResultList().stream().findFirst().orElse(null);
	}

}
