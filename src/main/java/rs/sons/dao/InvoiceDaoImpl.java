package rs.sons.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import rs.sons.entity.Invoice;
import rs.sons.entity.User;
import rs.sons.helper.MyDateFormatter;

@Repository(value = "invoiceDao")
@Transactional
public class InvoiceDaoImpl implements InvoiceDao {
	
	private static final String INVOICE = "invoice";
	
	//https://vladmihalcea.com/how-to-map-a-manytoone-association-using-a-non-primary-key-column/
	
	@PersistenceContext
	EntityManager em;

	public void saveInvoice(Invoice invoice) {
		em.persist(invoice);
	}

	@Override
	public List<Invoice> getAllInvoicesForClient(Integer clientId) {
		
		
		
		//TypedQuery<Invoice> query = em.createQuery("FROM Invoice WHERE invoice_client_id = :clientId", Invoice.class);moze i vako
		TypedQuery<Invoice> query = em.createQuery("FROM Invoice i WHERE i.client.client_id = :clientId", Invoice.class);
		query.setParameter("clientId", clientId);
		  
		return query.getResultList();
		 
		 
		/*
		 * Query query = em.
		 * createQuery("SELECT i FROM Invoice i JOIN FETCH i.invoiceItems ii WHERE i.client.client_id = :clientId"
		 * ); query.setParameter("clientId", clientId);
		 * 
		 * return (List<Invoice>) query.getResultList();
		 */
		 
	}

	public int getDocumentNumber(String type, int year, int month) {
		
		TypedQuery<Integer> query;
		
		if(type.equalsIgnoreCase(INVOICE)) {
			
			query = em.createQuery("SELECT MAX(i.invoice_number) + 1 FROM Invoice i WHERE i.invoice_year = :year AND i.invoice_month = :month", Integer.class);
			query.setParameter("year", year);
			query.setParameter("month", month);
			
		} else {
			
			query = em.createQuery("SELECT MAX(i.invoice_preinvoice_number) + 1 FROM Invoice i WHERE i.invoice_preinvoice_year = :year AND i.invoice_preinvoice_month = :month", Integer.class);
			query.setParameter("year", year);
			query.setParameter("month", month);
			
		}
		
		if(query.getSingleResult() == null) {
			return 1;
		} else {
			return query.getSingleResult();
		}
		
	}

	@SuppressWarnings("unchecked")
	public Invoice getInvoiceById(Long invoiceId) {
		
		Query query = em.createQuery("FROM Invoice WHERE invoice_id = :id");
		query.setParameter("id", invoiceId);
		
		return (Invoice) query.getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public void deleteInvoiceById(Long invoiceId) {

		Invoice invoice = em.find(Invoice.class, invoiceId);
		
		if(invoice != null) {
			em.remove(invoice);
			
		}
	}

	@Override
	public void createInvoiceWithPayment(Long invoiceId, String paymentDate, String deliveryDate, User user) {
		
		TypedQuery<Invoice> query = em.createQuery("FROM Invoice WHERE invoice_id = :invoiceId AND invoice_payed = false", Invoice.class);
		query.setParameter("invoiceId", invoiceId);
		
		Invoice invoice = query.getResultList().stream().findFirst().orElse(null);
		
		if(invoice != null) {
			invoice.setInvoice_delivery_date(MyDateFormatter.dateFormatDash(deliveryDate));
			invoice.setInvoice_payment_date_for_print(MyDateFormatter.dateFormatDash(paymentDate));
			invoice.setInvoice_payment_date_real(new Date());
			invoice.setInvoice_is_invoice(true);
			invoice.setInvoice_month(MyDateFormatter.getMonthFromDate(MyDateFormatter.dateFormatDash(paymentDate))); //mesec fakture ce biti odredjen u odnosu na uneti paymentDate
			invoice.setInvoice_year(MyDateFormatter.getYearFromDate(MyDateFormatter.dateFormatDash(paymentDate))); //godina fakture ce biti odredjen u odnosu na uneti paymentDate
			invoice.setInvoice_number(this.getDocumentNumber("invoice", MyDateFormatter.getYearFromDate(MyDateFormatter.dateFormatDash(paymentDate)), MyDateFormatter.getMonthFromDate(MyDateFormatter.dateFormatDash(paymentDate))));
			invoice.setInvoice_payed(true);
			invoice.setUserApplied(user);
			
			em.merge(invoice);
		}
	}

	@Override
	public void createInvoiceWithoutPayment(Long invoiceId, String deliveryDate, User user) {
		
		TypedQuery<Invoice> query = em.createQuery("FROM Invoice WHERE invoice_is_invoice = false AND invoice_id = :invoiceId AND invoice_payed = false", Invoice.class);
		query.setParameter("invoiceId", invoiceId);
		
		Invoice invoice = query.getResultList().stream().findFirst().orElse(null);
		
		if(invoice != null) {
			invoice.setInvoice_delivery_date(MyDateFormatter.dateFormatDash(deliveryDate));
			invoice.setInvoice_is_invoice(true);
			invoice.setInvoice_month(MyDateFormatter.getMonthFromDate(new Date()));
			invoice.setInvoice_year(MyDateFormatter.getYearFromDate(new Date()));
			invoice.setInvoice_number(this.getDocumentNumber("invoice", MyDateFormatter.getYearFromDate(new Date()), MyDateFormatter.getMonthFromDate(new Date())));
			invoice.setUserApplied(user);
			
			em.merge(invoice);
		}
	}

	@Override
	public void applyPaymentOnExistingInvoice(Long invoiceId, String paymentDate) {
		
		TypedQuery<Invoice> query = em.createQuery("FROM Invoice WHERE invoice_is_invoice = true AND invoice_id = :invoiceId AND invoice_payed = false", Invoice.class);
		query.setParameter("invoiceId", invoiceId);
		
		Invoice invoice = query.getResultList().stream().findFirst().orElse(null);
		
		if(invoice != null) {
			invoice.setInvoice_payment_date_for_print(MyDateFormatter.dateFormatDash(paymentDate));
			invoice.setInvoice_payment_date_real(new Date());
			invoice.setInvoice_payed(true);
			
			em.merge(invoice);
		}
		
	}

}
