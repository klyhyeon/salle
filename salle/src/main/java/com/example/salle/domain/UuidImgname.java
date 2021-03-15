package com.example.salle.domain;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UuidImgname {
	
	
	public String makeFilename(String filename) {
		UUID uid = UUID.randomUUID();
		
		String newFilename = uid + "_" + filename; 
		
		return newFilename;
	}

}
