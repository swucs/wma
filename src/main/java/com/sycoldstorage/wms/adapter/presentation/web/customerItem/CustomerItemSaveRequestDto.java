package com.sycoldstorage.wms.adapter.presentation.web.customerItem;

import lombok.Data;

/**
 * 거래처별 품목 저장 Dto
 */
@Data
public class CustomerItemSaveRequestDto {
    private Long id;
    private Long customerId;
    private Long itemId;
    private Long storageFeeId;
}
