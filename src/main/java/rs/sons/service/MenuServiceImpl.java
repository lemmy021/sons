package rs.sons.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.sons.dao.MenuDao;
import rs.sons.entity.Menu;

@Service("menuService")
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	MenuDao menuDao;
	@Override
	public List<Menu> getLeftMenuItems() {
		return menuDao.getLeftMenuItems();
	}
	
	public boolean UrlExistsInMenuItemTable(String url) {
		return menuDao.UrlExistsInMenuItemTable(url);
	}

}
