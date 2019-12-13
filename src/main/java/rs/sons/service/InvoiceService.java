package rs.sons.service;

import java.util.List;

import rs.sons.entity.Invoice;
import rs.sons.entity.User;

public interface InvoiceService {

	public void saveInvoice(Invoice invoice);
	
	public List<Invoice> getAllInvoicesForClient(Integer clientId);
	
	public int getDocumentNumber(String type, int year, int month);
	
	public Invoice getInvoiceById(Long invoiceId);
	
	public void deleteInvoiceById(Long invoiceId);
	
	public void createInvoiceWithPayment(Long invoiceId, String paymentDate, String deliveryDate, User user);
	
	public void createInvoiceWithoutPayment(Long invoiceId, String deliveryDate, User user);
	
	public void applyPaymentOnExistingInvoice(Long invoiceId, String paymentDate);
}
