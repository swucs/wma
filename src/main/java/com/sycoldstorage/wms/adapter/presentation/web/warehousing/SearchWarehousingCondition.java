package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import com.sycoldstorage.wms.domain.warehousing.WarehousingType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SearchWarehousingCondition {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate baseDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate baseDateTo;

    private String customerName;
    private String itemName;
    private String warehousingTypeValue;

    public WarehousingType getWarehousingType() {

        if (this.warehousingTypeValue == null) {
            return null;
        }

        try {
            return WarehousingType.valueOf(this.warehousingTypeValue);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
