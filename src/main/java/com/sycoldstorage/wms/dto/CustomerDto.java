package com.sycoldstorage.wms.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CustomerDto {
    private Long id;
    private String name;
    private String businessNumber;
    private String representativeName;
    private String businessConditions;
    private String typeOfBusiness;
    private String address;
    private String phoneNumber;
    private String faxNumber;
    private boolean use;

    @QueryProjection //순수한 모델이 아니라고 판단될 수도 있어서 선택의 문제가 있을 수 있다.
    public CustomerDto(Long id, String name, String businessNumber, String representativeName, String businessConditions, String typeOfBusiness, String address, String phoneNumber, String faxNumber, boolean use) {
        this.id = id;
        this.name = name;
        this.businessNumber = businessNumber;
        this.representativeName = representativeName;
        this.businessConditions = businessConditions;
        this.typeOfBusiness = typeOfBusiness;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
        this.use = use;
    }
}
