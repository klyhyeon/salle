package com.salle.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.salle.domain.Login;

public class LoginCheckEmptyAndEmailValidation implements Validator {
	
	    @Override
	    public void validate(Object target, Errors errors) {
	        Login loginInfo = (Login) target;
	        if (loginInfo.getEmail() == null)
	        	errors.rejectValue("email", "unregistered");
	        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"password","required");
	        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"email","required");
	    }

		@Override
		public boolean supports(Class<?> clazz) {
			return Login.class.isAssignableFrom(clazz);
		}

}
