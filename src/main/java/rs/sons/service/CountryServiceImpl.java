package rs.sons.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.sons.dao.CountryDao;
import rs.sons.entity.Country;

@Service("countryService")
public class CountryServiceImpl implements CountryService {
	
	@Autowired
	CountryDao countryDao;

	@Override
	public List<Country> getAllCountries() {
		return countryDao.getAllCountries();
	}

}
