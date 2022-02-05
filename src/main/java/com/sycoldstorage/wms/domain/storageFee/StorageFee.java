package com.sycoldstorage.wms.domain.storageFee;

import com.sycoldstorage.wms.adapter.presentation.web.storageFee.StorageFeeDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * 보관료
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@EqualsAndHashCode(of="id")
@Entity
public class StorageFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate baseDate;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double storage;

    @Column(nullable = false)
    private double loading;

    /**
     * 생성자
     * @param storageFeeDto
     */
    public StorageFee(StorageFeeDto storageFeeDto) {
        changeStorageFee(storageFeeDto);
    }

    /**
     * 보관료 데이터 update
     * @param storageFeeDto
     */
    public void changeStorageFee(StorageFeeDto storageFeeDto) {
        this.baseDate = storageFeeDto.getBaseDate();
        this.name = storageFeeDto.getName();
        this.storage = storageFeeDto.getStorage();
        this.loading = storageFeeDto.getLoading();
    }

    /**
     * StorageFeeDto로 변환
     * @return
     */
    public StorageFeeDto toStorageFeeDto() {
        return StorageFeeDto.builder()
                .id(this.id)
                .baseDate(this.baseDate)
                .name(this.name)
                .storage(this.storage)
                .loading(this.loading)
                .build();
    }

}
