package rs.sons.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Controller
public class SalesController {
	// https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html#dynamic-fields

	@ModelAttribute("preinvoice")
	public PreInvoice constructList() {
		PreInvoice preInvoice = new PreInvoice();

		preInvoice.setClient_id("888888888");

		Item item = new Item();
		item.setService("");
		item.setAmount("");
		item.setUnit("");
		item.setPrice("");

		List<Item> itemList = new ArrayList<Item>();
		itemList.add(item);

		preInvoice.setItems(itemList);
		
		return preInvoice;
	}

	@RequestMapping(value = "/sale/{jwtId:.+}")
	public String showPreInvoiceForm(Model model) {
		return "sale";
	}
	
	@PostMapping(value="/sale", params={"addItem"})
	public String addRow(@Valid @ModelAttribute("preinvoice") @Validated PreInvoice preInvoice, BindingResult bindingResult) {
	    
		if(bindingResult.hasErrors()) {
			System.out.println("11111111111111111111");
			return "sale";
		}
		
		preInvoice.getItems().add(new Item());
	    
	    return "sale";
	}
	
	@PostMapping(value="/sale", params={"removeItem"})
	public String removeRow(@ModelAttribute("preinvoice") PreInvoice preInvoice, final BindingResult bindingResult, HttpServletRequest req) {
	    
		final Integer rowId = Integer.valueOf(req.getParameter("removeItem"));
		preInvoice.getItems().remove(rowId.intValue());
	    
	    return "sale";
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
