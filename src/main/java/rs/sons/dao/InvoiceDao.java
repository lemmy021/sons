package rs.sons.dao;

import java.util.List;

import rs.sons.entity.Invoice;

public interface InvoiceDao {

	public void saveInvoice(Invoice invoice);
	
	public List<Invoice> getAllInvoicesForClient(Integer clientId);
	
	public int getDocumentNumber(String type, int year, int month);
}
