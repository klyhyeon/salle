package com.salle.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JasyptConfigTest {
	
	@Autowired
	JasyptConfig jasyptConfig;

	@Test
    public void verifyPropertiesEncrypt() {
        String encrypt = jasyptConfig.getPwd();
        assertEquals("salle067138", encrypt);
    }

}
