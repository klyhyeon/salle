package com.salle.domain;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor //기본생성자
@Component
public @Data class Product {

	private String pr_email;
	private Timestamp pr_reg_date;
	private String pr_img_1;
	private String pr_img_2;
	private String pr_img_3;
	private String pr_img_4;
	private String pr_img_5;
	private String pr_title;
	private String pr_category;
	private String pr_region1;
	private String pr_region2;	
	private String pr_price;
	private String pr_detail;
	private int pr_id; //seq 생성
	private int hoursFromUpload;
	private String pr_title_alias;
}
