package rs.sons.service;

import java.util.List;

import rs.sons.entity.Invoice;

public interface InvoiceService {

	public void saveInvoice(Invoice invoice);
	
	public List<Invoice> getAllInvoicesForClient(Integer clientId);
	
	public int getDocumentNumber(String type, int year, int month);
	
	public Invoice getInvoiceById(Long invoiceId);
}
