package com.sycoldstorage.wms.adapter.presentation.web.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemSelectBoxDto {

    private Long id;
    private String name;
    private Double unitWeight;
    private String unitName;

    public ItemSelectBoxDto(Long id, String name, Double unitWeight, String unitName) {
        this.id = id;
        this.name = name;
        this.unitWeight = unitWeight;
        this.unitName = unitName;
    }
}
