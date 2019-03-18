package rs.sons.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import rs.sons.entity.Invoice;

@Repository(value = "invoiceDao")
@Transactional
public class InvoiceDaoImpl implements InvoiceDao {
	
	@PersistenceContext
	EntityManager em;

	public void saveInvoice(Invoice invoice) {
		em.persist(invoice);
	}

}
