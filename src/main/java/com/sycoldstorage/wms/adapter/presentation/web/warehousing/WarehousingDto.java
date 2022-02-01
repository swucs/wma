package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * 입출고 목록화면 Dto
 */
@Data
public class WarehousingDto {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate baseDate;
    private Long customerId;
    private String customerName;
    private String name;
    private String warehousingTypeName;
    private String warehousingTypeValue;
    private String quickFrozenYn;

    public WarehousingDto(Long id, LocalDate baseDate, Long customerId, String customerName, String name, String warehousingTypeName, String warehousingTypeValue, String quickFrozenYn) {
        this.id = id;
        this.baseDate = baseDate;
        this.customerId = customerId;
        this.customerName = customerName;
        this.name = name;
        this.warehousingTypeName = warehousingTypeName;
        this.warehousingTypeValue = warehousingTypeValue;
        this.quickFrozenYn = quickFrozenYn;
    }
}
