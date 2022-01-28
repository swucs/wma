package com.sycoldstorage.wms.adapter.presentation.web.item;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemDto {

    private Long id;
    private String name;
    private Double unitWeight;
    private String unitName;
    private LocalDateTime registerdDate;
    private String remarks;

    /**
     * QueryDsl에서 사용됨
     * @param id
     * @param name
     * @param unitWeight
     * @param unitName
     * @param registerdDate
     * @param remarks
     */
    public ItemDto(Long id, String name, double unitWeight, String unitName, LocalDateTime registerdDate, String remarks) {
        this.id = id;
        this.name = name;
        this.unitWeight = unitWeight;
        this.unitName = unitName;
        this.registerdDate = registerdDate;
        this.remarks = remarks;
    }
}
