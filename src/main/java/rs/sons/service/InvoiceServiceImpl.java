package rs.sons.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.sons.dao.InvoiceDao;
import rs.sons.entity.Invoice;
import rs.sons.entity.User;

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

	public Invoice getInvoiceById(Long invoiceId) {
		return invoiceDao.getInvoiceById(invoiceId);
	}

	@Override
	public void deleteInvoiceById(Long invoiceId) {
		invoiceDao.deleteInvoiceById(invoiceId);
	}

	@Override
	public void createInvoiceWithPayment(Long invoiceId, String paymentDate, String deliveryDate, User user) {
		invoiceDao.createInvoiceWithPayment(invoiceId, paymentDate, deliveryDate, user);
	}

	@Override
	public void createInvoiceWithoutPayment(Long invoiceId, String deliveryDate, User user) {
		invoiceDao.createInvoiceWithoutPayment(invoiceId, deliveryDate, user);
	}

	@Override
	public void applyPaymentOnExistingInvoice(Long invoiceId, String paymentDate) {
		invoiceDao.applyPaymentOnExistingInvoice(invoiceId, paymentDate);
	}

}
