package com.sycoldstorage.wms.adapter.presentation.web.storageFee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StorageFeeDto {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate baseDate;

    private String name;

    private Double storage;

    private Double loading;

    /**
     * QueryDsl
     * @param id
     * @param baseDate
     * @param name
     * @param storage
     * @param loading
     */
    public StorageFeeDto(Long id, LocalDate baseDate, String name, Double storage, Double loading) {
        this.id = id;
        this.baseDate = baseDate;
        this.name = name;
        this.storage = storage;
        this.loading = loading;
    }
}
