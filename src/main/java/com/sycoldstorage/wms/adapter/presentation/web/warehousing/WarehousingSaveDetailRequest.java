package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import lombok.Data;

@Data
public class WarehousingSaveDetailRequest {
    private Long id;
    private Long itemId;
    private Double totalWeight;
    private String remarks;
    private String calculationYn;
}
