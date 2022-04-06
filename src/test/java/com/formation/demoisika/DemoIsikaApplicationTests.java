package com.formation.demoisika;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.formation.demoisika.service.CalculatorService;

@SpringBootTest
class DemoIsikaApplicationTests {

	@Autowired
	private CalculatorService calcul;
	
	@Test
	void addTest() {
		assertEquals(12, calcul.add(8, 4));
	}
	
}
