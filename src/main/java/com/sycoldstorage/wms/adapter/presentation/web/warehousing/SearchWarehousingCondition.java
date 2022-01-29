package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import com.sycoldstorage.wms.domain.warehousing.WarehousingType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchWarehousingCondition {

    private LocalDate baseDateFrom;
    private LocalDate baseDateTo;
    private String customerName;
    private String itemName;
    private String warehousingTypeText;

    public WarehousingType getWarehousingType() {

        if (this.warehousingTypeText == null) {
            return null;
        }

        try {
            return WarehousingType.valueOf(this.warehousingTypeText);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
