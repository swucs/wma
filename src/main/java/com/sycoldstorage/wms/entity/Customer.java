package com.sycoldstorage.wms.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 거래처 Entity
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String businessNumber;

    @Column(nullable = false)
    private String representativeName;

    private String businessConditions;

    private String typeOfBusiness;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    private String faxNumber;

    @Column(nullable = false)
    private boolean use;

    @Column(nullable = false)
    private boolean deleted;
}

