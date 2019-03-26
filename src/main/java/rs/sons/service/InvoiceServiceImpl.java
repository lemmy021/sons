package rs.sons.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.sons.dao.InvoiceDao;
import rs.sons.entity.Invoice;

@Service("invoiceService")
public class InvoiceServiceImpl implements InvoiceService {
	
	@Autowired
	InvoiceDao invoiceDao;

	public void saveInvoice(Invoice invoice) {
		invoiceDao.saveInvoice(invoice);
	}

	public List<Invoice> getAllInvoicesForClient(Integer clientId) {
		return invoiceDao.getAllInvoicesForClient(clientId);
	}

	public int getDocumentNumber(String type, int year, int month) {
		return invoiceDao.getDocumentNumber(type, year, month);
	}

}
