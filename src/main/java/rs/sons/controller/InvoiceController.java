package rs.sons.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import rs.sons.entity.Client;
import rs.sons.entity.Invoice;
import rs.sons.jwt.JwtHelper;
import rs.sons.service.ClientService;
import rs.sons.service.InvoiceService;

@Controller
public class InvoiceController {
	
	@Autowired
	ClientService clientService;
	
	@Autowired
	InvoiceService invoiceService;

	@GetMapping("/invoices/{jwtId:.+}")
	public String showAllInvoicesForUser(Model model, @PathVariable("jwtId") String jwtId) {

		Integer clientId = JwtHelper.decodeJWT(jwtId);

		if (clientId > 0) {
			Client clientDb = clientService.findClientById(clientId);

			if (clientDb != null) {
				
				List<Invoice> invoices = invoiceService.getAllInvoicesForClient(clientId);
				
				//invoice.setClient(clientDb);
				//invoice.setInvoice_client_id_jwt_helper(jwtId);
				model.addAttribute("invoices", invoices);
			}
		}

		return "invoicesforuser";
	}

}
