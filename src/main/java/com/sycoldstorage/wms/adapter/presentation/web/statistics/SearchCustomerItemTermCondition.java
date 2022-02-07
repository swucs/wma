package com.sycoldstorage.wms.adapter.presentation.web.statistics;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class SearchCustomerItemTermCondition {

    @NotNull(message = "고객을 선택하세요.")
    private Long customerId;

    @NotNull(message = "기준일자를 입력하세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate baseDateFrom;

    @NotNull(message = "기준일자를 입력하세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate baseDateTo;
}
