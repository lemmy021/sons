package rs.sons.interceptor;

import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import rs.sons.entity.Menu;
import rs.sons.entity.MenuItem;
import rs.sons.entity.User;
import rs.sons.service.MenuService;
import rs.sons.service.UserService;
import rs.sons.service.WeatherService;

public class AclInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	WeatherService weatherService;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("preHandle URI= " + request.getRequestURI());
		
		try {
			SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			System.out.println("ime= " + SecurityContextHolder.getContext().getAuthentication().getName());
		} catch (Exception e) {
			System.out.println("niko nije logovan");
		}
		
		/*
		 * HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		
		 * System.out.println("kontroler= " +
		 * handlerMethod.getBeanType().getSimpleName().replace("Controller", ""));
		 * System.out.println("metod= " + handlerMethod.getMethod().getName());
		 */
		
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO ovde treba implamentirati meni, sesiju i vremensku prognozu
		
		HttpSession session = request.getSession();
		
		Enumeration<String> attributes = request.getSession().getAttributeNames();
		while (attributes.hasMoreElements()) {
			String attribute = (String) attributes.nextElement();
			System.out.println(attribute + " : " + request.getSession().getAttribute(attribute));
		}
		 
		
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		if(!handlerMethod.getMethod().getName().contains("ajax")) {
			
			//treba mi zbog prve - login stranice
			if(Objects.isNull(session.getAttribute("menu_item"))) {
				session.setAttribute("menu_item", "/");
				session.setAttribute("user_edit", false);
			}
			
			if((boolean) session.getAttribute("user_edit")) {
				User loggedUser = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
				
				session.setAttribute("user_name_surname", loggedUser.getUser_name() + " " + loggedUser.getUser_surname());
				session.setAttribute("user_email", loggedUser.getUser_email());
				session.setAttribute("user_gender_name", loggedUser.getGender().getGender_name());
				session.setAttribute("user_edit", false);
				
			}
			
			List<Menu> menus = menuService.getLeftMenuItems();
			
			boolean urlExistsInTable = menuService.UrlExistsInMenuItemTable(request.getRequestURI());
			
			for(Menu menu : menus) {
				for(MenuItem menuItem : menu.getMenuItems()) {
					if(urlExistsInTable) {
						if(menuItem.getMenu_item_url().equalsIgnoreCase(request.getRequestURI())) {
							menu.setMenu_css_active(true);
							menuItem.setMenu_item_css_active(true);
							
							session.setAttribute("menu_item", request.getRequestURI());
							
							break;
						}
					} else {
						if(menuItem.getMenu_item_url().equalsIgnoreCase(session.getAttribute("menu_item").toString())) {
							menu.setMenu_css_active(true);
							menuItem.setMenu_item_css_active(true);
							
							break;
						}
					}
				}
			}
			
			modelAndView.addObject("weather",weatherService.getNsWeather());
			modelAndView.addObject("menuall", menus);
		}
	}
	
}
