package rs.sons.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.sons.entity.Invoice;
import rs.sons.entity.InvoiceItem;

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
	
	@GetMapping("/sale")
	public String showPreInvoiceFormTest() {
		return "invoiceform";
	}
	
	@PostMapping(value="/sale", params={"addItem"})
	public String addRowtess(@ModelAttribute("invoice") Invoice invoice, BindingResult bindingResult) {
	    
		if(bindingResult.hasErrors()) {
			return "invoiceform";
		}
		
		invoice.getInvoiceItems().add(new InvoiceItem());
	    
	    return "invoiceform";
	}
	
	@PostMapping(value="/sale", params={"removeItem"})
	public String removeRowTess(@ModelAttribute("invoice") Invoice invoice, final BindingResult bindingResult, HttpServletRequest req) {
	    
		final Integer rowId = Integer.valueOf(req.getParameter("removeItem"));
		invoice.getInvoiceItems().remove(rowId.intValue());
	    
	    return "invoiceform";
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
