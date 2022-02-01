package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import lombok.Data;

@Data
public class WarehousingDetailDto {
    private Long id;
    private Long itemId;
    private String itemName;
    private Double itemUnitWeight;
    private String itemUnitName;
    private Double count;
    private Double remainingWeight;
    private Double totalWeight;
    private String remarks;
    private String calculationYn;

    public WarehousingDetailDto(Long id, Long itemId, String itemName, Double itemUnitWeight, String itemUnitName, Double count
            , Double remainingWeight, Double totalWeight, String remarks, String calculationYn) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemUnitWeight = itemUnitWeight;
        this.itemUnitName = itemUnitName;
        this.count = count;
        this.remainingWeight = remainingWeight;
        this.totalWeight = totalWeight;
        this.remarks = remarks;
        this.calculationYn = calculationYn;
    }
}
