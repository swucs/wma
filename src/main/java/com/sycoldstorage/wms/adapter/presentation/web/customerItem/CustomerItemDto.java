package com.sycoldstorage.wms.adapter.presentation.web.customerItem;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 고객별품목별보관료정보
 */
@Data
@AllArgsConstructor
public class CustomerItemDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long itemId;
    private String itemName;
    private Double itemUnitWeight;
    private String itemUnitName;
    private Long storageFeeId;
    private String storageFeeName;
}

