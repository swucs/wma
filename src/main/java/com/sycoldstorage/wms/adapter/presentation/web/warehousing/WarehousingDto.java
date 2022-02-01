package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sycoldstorage.wms.domain.warehousing.WarehousingType;
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
    private WarehousingType warehousingType;
    private String quickFrozenYn;

    public WarehousingDto(Long id, LocalDate baseDate, Long customerId, String customerName, String name, String warehousingTypeName, WarehousingType warehousingType, String quickFrozenYn) {
        this.id = id;
        this.baseDate = baseDate;
        this.customerId = customerId;
        this.customerName = customerName;
        this.name = name;
        this.warehousingTypeName = warehousingTypeName;
        this.warehousingType = warehousingType;
        this.quickFrozenYn = quickFrozenYn;
    }

    public String getWarehousingTypeText() {
        return this.warehousingType.toString();
    }
}
