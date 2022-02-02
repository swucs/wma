package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 입출고 저장 요청정보
 */
@Data
public class WarehousingSaveRequest {

    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate baseDate;
    private Long customerId;
    private String name;
    private String warehousingTypeValue;
    private String quickFrozenYn;
    List<WarehousingSaveDetailRequest> warehousingDetails;
}
