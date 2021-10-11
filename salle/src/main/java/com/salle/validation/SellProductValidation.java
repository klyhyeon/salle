package com.salle.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.salle.domain.Product;

public class SellProductValidation implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Product.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		//target: 검사 대상 객체 참조(커맨드 객체) = Product 객체
		//errors : target 검사 후 에러 코드를 저장하는 객체(스프링이 자동 생성)
		
		Product product = (Product) target;
		
		if (product.getPr_img_1() == null) {
			errors.rejectValue("pr_img_1", "required");
		}
		
		//어떤 정보가 비어있을 경우
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"pr_title", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"pr_region1", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"pr_price", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"pr_detail", "required");

	}

}
