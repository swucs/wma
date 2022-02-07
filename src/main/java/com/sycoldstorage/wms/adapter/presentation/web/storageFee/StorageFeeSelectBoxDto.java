package com.sycoldstorage.wms.adapter.presentation.web.storageFee;

import lombok.Data;

@Data
public class StorageFeeSelectBoxDto {
    private Long id;
    private String name;

    public StorageFeeSelectBoxDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
