package com.sycoldstorage.wms.adapter.presentation.web.item;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime registeredDate;

    private String remarks;

    /**
     * QueryDsl에서 사용됨
     * @param id
     * @param name
     * @param unitWeight
     * @param unitName
     * @param registeredDate
     * @param remarks
     */
    public ItemDto(Long id, String name, double unitWeight, String unitName, LocalDateTime registeredDate, String remarks) {
        this.id = id;
        this.name = name;
        this.unitWeight = unitWeight;
        this.unitName = unitName;
        this.registeredDate = registeredDate;
        this.remarks = remarks;
    }
}
