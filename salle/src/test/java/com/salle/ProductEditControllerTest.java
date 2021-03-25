package com.salle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.salle.application.ProductEditService;
import com.salle.controller.ProductEditController;

@WebMvcTest(ProductEditController.class)
public class ProductEditControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	ProductEditService productEditservice;
	
	@Test
	public void productEditImg() {
		
		
	}
	
}
