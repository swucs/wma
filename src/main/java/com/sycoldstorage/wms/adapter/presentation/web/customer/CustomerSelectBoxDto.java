package com.sycoldstorage.wms.adapter.presentation.web.customer;

import lombok.Data;

@Data
public class CustomerSelectBoxDto {
    private Long id;
    private String name;

    public CustomerSelectBoxDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
