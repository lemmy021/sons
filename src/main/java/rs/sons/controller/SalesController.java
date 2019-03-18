package rs.sons.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.sons.entity.Client;
import rs.sons.entity.Invoice;
import rs.sons.entity.InvoiceItem;
import rs.sons.entity.User;
import rs.sons.jwt.JwtHelper;
import rs.sons.service.ClientService;
import rs.sons.service.InvoiceService;
import rs.sons.service.UserService;

@Controller
public class SalesController {
	//http://programmertech.com/program/jee/spring-mvc-form-validation-with-thymeleaf
	// https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html#dynamic-fields

	/*
	 * @ModelAttribute("preinvoice") public PreInvoice constructList() { PreInvoice
	 * preInvoice = new PreInvoice();
	 * 
	 * preInvoice.setClient_id("888888888");
	 * 
	 * Item item = new Item(); item.setService(""); item.setAmount("");
	 * item.setUnit(""); item.setPrice("");
	 * 
	 * List<Item> itemList = new ArrayList<Item>(); itemList.add(item);
	 * 
	 * preInvoice.setItems(itemList);
	 * 
	 * return preInvoice; }
	 */
	
	@Autowired
	ClientService clientService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	InvoiceService invoiceService;
	
	@ModelAttribute("invoice")
	public Invoice constructPreInvoiceFormFields() {
		
		Invoice invoice = new Invoice();
		InvoiceItem invoiceItem = new InvoiceItem();
		List<InvoiceItem> invoiceItemList = new ArrayList<InvoiceItem>();
		
		invoiceItemList.add(invoiceItem);
		invoice.setInvoiceItems(invoiceItemList);
		
		return invoice;
	}

	/*
	 * @GetMapping(value = "/sale/{jwtId:.+}") public String
	 * showPreInvoiceForm(Model model) { return "sale"; }
	 * 
	 * @PostMapping(value="/sale", params={"addItem"}) public String
	 * addRow(@Valid @ModelAttribute("preinvoice") @Validated PreInvoice preInvoice,
	 * BindingResult bindingResult) {
	 * 
	 * if(bindingResult.hasErrors()) { return "sale"; }
	 * 
	 * preInvoice.getItems().add(new Item());
	 * 
	 * return "sale"; }
	 * 
	 * @PostMapping(value="/sale", params={"removeItem"}) public String
	 * removeRow(@ModelAttribute("preinvoice") PreInvoice preInvoice, final
	 * BindingResult bindingResult, HttpServletRequest req) {
	 * 
	 * final Integer rowId = Integer.valueOf(req.getParameter("removeItem"));
	 * preInvoice.getItems().remove(rowId.intValue());
	 * 
	 * return "sale"; }
	 */
	
	@GetMapping("/sale/{jwtId:.+}")
	public String showPreInvoiceFormTest(@ModelAttribute("invoice") Invoice invoice, @PathVariable("jwtId") String jwtId) {
		
		Integer clientId = JwtHelper.decodeJWT(jwtId);
		
		if (clientId > 0) {
			Client clientDb = clientService.findClientById(clientId);

			if (clientDb != null) {
				invoice.setInvoice_client_id_jwt_helper(jwtId);
			}
		}
		
		return "invoiceform";
	}
	
	@PostMapping(value="/sale", params={"addItem"})
	public String addRowToPreInvoiceForm(@Valid @ModelAttribute("invoice") Invoice invoice, BindingResult bindingResult) {
	    
		if(bindingResult.hasErrors()) {
			return "invoiceform";
		}
		
		invoice.getInvoiceItems().add(new InvoiceItem());
	    
	    return "invoiceform";
	}
	
	@PostMapping(value="/sale", params={"removeItem"})
	public String removeRowFromPreInvoiceForm(@ModelAttribute("invoice") Invoice invoice, final BindingResult bindingResult, HttpServletRequest req) {
	    
		final Integer rowId = Integer.valueOf(req.getParameter("removeItem"));
		invoice.getInvoiceItems().remove(rowId.intValue());
	    
	    return "invoiceform";
	}
	
	@PostMapping("/sale")
	public String savePreInvoice(@Valid @ModelAttribute("invoice") Invoice invoice, BindingResult bindingResult, Principal principal) {
		
		if(bindingResult.hasErrors()) {
			return "invoiceform";
		}
		
		Integer clientId = JwtHelper.decodeJWT(invoice.getInvoice_client_id_jwt_helper());
		
		if (clientId > 0) {
			Client clientDb = clientService.findClientById(clientId);
			
			if (clientDb != null) {
				invoice.setClient(clientDb);
				
				User user = userService.findUserByUsername(principal.getName());
				
				invoice.setUser(user);
				invoice.setUserApplied(user);
				
				invoice.setInvoice_client_data(setClientData(clientDb));
				
				
				invoiceService.saveInvoice(invoice);
			}
		}
		
		return "redirect:/client/" + invoice.getInvoice_client_id_jwt_helper();
	}
	
	private String setClientData(Client client) {
		
		StringBuilder sb = new StringBuilder();
		
		if(client.isClient_type() == true) {
			sb.append(client.getClient_company());
			sb.append(";");
		} else {
			sb.append(client.getClient_name());
			sb.append(" ");
			sb.append(client.getClient_surname());
			sb.append(";");
		}
		
		sb.append(client.getClient_address_street());
		sb.append(";");
		sb.append(client.getClient_address_zip());
		sb.append(" ");
		sb.append(client.getClient_address_city());
		sb.append(";");
		sb.append(client.getCountry().getCountry_name());
		
		return sb.toString();
	}
	

	@Getter
	@Setter
	@NoArgsConstructor
	public static class PreInvoice {
		private String client_id; // Just some field.
		private List<Item> items;

		// Getters and setters are omitted.
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class Item {
		
		private String service;
		private String amount;
		private String unit;
		private String price;

		// Getters and setters are omitted.
	}

}
