package rs.sons.service;

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

}
