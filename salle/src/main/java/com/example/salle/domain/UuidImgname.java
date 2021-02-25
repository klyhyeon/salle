package com.example.salle.domain;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UuidImgname {
	
	UUID uid = UUID.randomUUID();
	
	public String makeFilename(String filename) {
		
		String newFilename = uid + "_" + filename; 
		
		return newFilename;
	}

}
