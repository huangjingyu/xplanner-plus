package net.sf.xplanner.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonObjectHandler {

	@RequestMapping("/{objectType}/list")
	public String list(@PathVariable String objectType){
		System.out.println(objectType);
		
		return objectType;
	}

	@RequestMapping("/{objectType}/edit/{objectId}")
	public String edit(@PathVariable String objectType, @PathVariable Integer objectId){
		System.out.println(objectType);
		return objectType;
	}
}
