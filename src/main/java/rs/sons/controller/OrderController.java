package rs.sons.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

//https://stackoverflow.com/questions/32196348/recommended-way-to-handle-thymeleaf-spring-mvc-ajax-forms-and-their-error-messag
//https://vkuzel.com/add-or-remove-items-from-a-list-of-objects-in-a-model-attribute-using-spring-mvc-and-thymeleaf

@Controller
class OrderController {

	@GetMapping(path = { "/order", "/order/{id}" })
	public String showOrder(@PathVariable(required = false) Long id, Model model) {
		Order order = new Order();

		order.setDate(new Date());
		Item item = new Item();
		item.setName("");
		List<Item> itemList = new ArrayList<Item>();
		itemList.add(item);
		order.setItems(itemList);

		model.addAttribute("order", order);
		return "order";
	}

	@PostMapping(value="/order", params={"addItem"})
	public String addRow(final Order order, final BindingResult bindingResult) {
	    
		order.getItems().add(new Item());
	    
	    return "order";
	}

	@PostMapping(value="/order", params={"removeItem"})
	public String removeRow(final Order order, final BindingResult bindingResult, final HttpServletRequest req) {
	    
		final Integer rowId = Integer.valueOf(req.getParameter("removeItem"));
	    order.getItems().remove(rowId.intValue());
	    
	    return "order";
	}

}