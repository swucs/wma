package com.sycoldstorage.wms.adapter.presentation.web.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
    private String useYn;

    /**
     * QueryDsl에서 사용됨
     * @param id
     * @param name
     * @param businessNumber
     * @param representativeName
     * @param businessConditions
     * @param typeOfBusiness
     * @param address
     * @param phoneNumber
     * @param faxNumber
     * @param useYn
     */
    public CustomerDto(Long id, String name, String businessNumber, String representativeName, String businessConditions, String typeOfBusiness, String address, String phoneNumber, String faxNumber, String useYn) {
        this.id = id;
        this.name = name;
        this.businessNumber = businessNumber;
        this.representativeName = representativeName;
        this.businessConditions = businessConditions;
        this.typeOfBusiness = typeOfBusiness;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
        this.useYn = useYn;
    }
}
