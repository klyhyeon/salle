package com.salle.domain;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT_IMAGE")
public class ProductImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String url; //AWS S3

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}