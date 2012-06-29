package net.sf.xplanner.web;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import net.sf.xplanner.dao.impl.CommonDao;


public class BasePage<T> {
	private final Class<T> domainClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	@Autowired
	private CommonDao<?> commonDao;
	
	public ModelAndView getModelAndView(String view, Serializable id){
		ModelAndView modelAndView = new ModelAndView(view);
		modelAndView.addObject(commonDao.getById(domainClass, id));
		return modelAndView;
	}
	
	
	public void setCommonDao(CommonDao<?> commonDao) {
		this.commonDao = commonDao;
	}
}
