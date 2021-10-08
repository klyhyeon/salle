package com.salle.domain;

import javax.persistence.*;

@Table(name = "PRODUCT_IMAGE")
@Entity
public class ProductImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String url; //AWS S3

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}