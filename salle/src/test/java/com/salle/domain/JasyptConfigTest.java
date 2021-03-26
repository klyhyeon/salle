package com.salle.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;

public class JasyptConfigTest {
	
	@Test
	public void basicJasypt() {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword("salle");
		String encrypted = "ENC(6+3bTmgFUiPZXXp2fu8y9m8/qIvyaOk1ysJKkdeP3f/9QtPsjJJSaZmUrkzWN6aD)";
		String encryptedTmp = encryptor.encrypt("salle067138");
		assertEquals("salle067138", encryptor.decrypt(encrypted));
	}
	
	

}
