package com.salle.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LoginCheckPwdValidation implements Validator {
	
	    @Override
	    public void validate(Object target, Errors errors) {
	        Boolean checkPwd = (Boolean) target;
	        if (!checkPwd)
            	errors.rejectValue("password", "incorrect");
	    }

		@Override
		public boolean supports(Class<?> clazz) {
			return Boolean.class.isAssignableFrom(clazz);
		}

}
