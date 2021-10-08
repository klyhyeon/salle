package com.salle.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "PRODUCT")
public class Product {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private int id; //자동생성
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private LocalDateTime deletedDate;
	private int status; //판매완료, 삭제, 판매중
	private String title;
	private String titleAlias;

	@Embedded
	private Address address;
	private int price;
	private String description;
	private int hoursFromUpload;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	@OneToMany(mappedBy = "product")
	private List<ProductImage> productImages;
}
