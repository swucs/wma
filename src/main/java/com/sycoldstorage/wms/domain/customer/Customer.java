package com.sycoldstorage.wms.domain.customer;


import com.sycoldstorage.wms.adapter.presentation.web.customer.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 거래처 Entity
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of="id")
@Entity
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

    public Customer(CustomerDto customerDto) {
        changeCustomer(customerDto);
        this.deleted = false;
    }


    /**
     * 삭제 플래그 처리
     */
    public void remove() {
        this.deleted = true;
    }

    /**
     * 거래처 정보 수정
     * @param customerDto
     */
    public void changeCustomer(CustomerDto customerDto) {
        this.name = customerDto.getName();
        this.businessNumber = customerDto.getBusinessNumber();
        this.representativeName = customerDto.getRepresentativeName();
        this.businessConditions = customerDto.getBusinessConditions();
        this.typeOfBusiness = customerDto.getTypeOfBusiness();
        this.address = customerDto.getAddress();
        this.phoneNumber = customerDto.getPhoneNumber();
        this.faxNumber = customerDto.getFaxNumber();
        this.use = "Y".equals(customerDto.getUseYn()) ? true : false;
    }

    /**
     * Dto 반환
     * @return
     */
    public CustomerDto toCustomerDto() {
        return CustomerDto.builder()
                .id(this.id)
                .name(this.name)
                .businessNumber(this.businessNumber)
                .representativeName(this.representativeName)
                .businessConditions(this.businessConditions)
                .typeOfBusiness(this.typeOfBusiness)
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .faxNumber(this.faxNumber)
                .useYn(this.use ? "Y" : "N")
                .build();
    }


}

