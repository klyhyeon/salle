package com.salle.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "PRODUCT")
public class Product {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private int id; //자동생성
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	private LocalDateTime deletedTime;
	private int status; //판매완료, 삭제, 판매중
	private String title;
	private String titleAlias;

	@Embedded
	private Address address;
	private int price;
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
	private List<ChatRoom> chatRooms;

	@OneToMany(mappedBy = "product")
	private List<ProductImage> productImages;
}
