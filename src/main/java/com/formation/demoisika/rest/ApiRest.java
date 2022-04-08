package com.formation.demoisika.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demoisika.service.CalculatorService;

@RestController
public class ApiRest {
	
	@Autowired
	private CalculatorService calcul;
	
	@RequestMapping("/")
	public String hello() {
		return "Hello world from SpringBoot V2";
	}
	
	@RequestMapping("/sum")
	public int add(@RequestParam("a") int a, @RequestParam("b") int b) {
		return calcul.add(a, b);
	}
}
