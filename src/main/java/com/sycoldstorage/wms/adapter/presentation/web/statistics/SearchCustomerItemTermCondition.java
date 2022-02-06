package com.sycoldstorage.wms.adapter.presentation.web.statistics;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SearchCustomerItemTermCondition {
    private Long customerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate baseDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate baseDateTo;
}
