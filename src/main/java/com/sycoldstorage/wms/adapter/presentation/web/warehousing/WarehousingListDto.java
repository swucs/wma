package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WarehousingListDto {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate baseDate;
    private String customerName;
    private String name;
    private String warehousingTypeName;
    private String quickFrozenName;

    public WarehousingListDto(Long id, LocalDate baseDate, String customerName, String name, String warehousingTypeName, String quickFrozenName) {
        this.id = id;
        this.baseDate = baseDate;
        this.customerName = customerName;
        this.name = name;
        this.warehousingTypeName = warehousingTypeName;
        this.quickFrozenName = quickFrozenName;
    }
}
