package com.sycoldstorage.wms.domain.warehousing;

import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingSaveDetailRequest;
import com.sycoldstorage.wms.domain.item.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * 입출고내역
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@Entity
public class WarehousingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehousing_id")
    private Warehousing warehousing;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private double totalWeight;

    @Column(nullable = false)
    private boolean calculation;

    private String remarks;

    @Column(nullable = false)
    private LocalDateTime registeredDate;

    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    /**
     *
     * @param warehousing
     * @param item
     * @param warehousingSaveDetailRequest
     */
    public WarehousingDetail(Warehousing warehousing, Item item, WarehousingSaveDetailRequest warehousingSaveDetailRequest) {
        this.warehousing = warehousing;
        this.item = item;
        this.totalWeight = warehousingSaveDetailRequest.getTotalWeight();
        this.calculation = "Y".equals(warehousingSaveDetailRequest.getCalculationYn());
        this.remarks = warehousingSaveDetailRequest.getRemarks();
        this.registeredDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }

    /**
     * 입출고 내역 수정
     * @param warehousingSaveDetailRequest
     */
    public void changeWarehousingDetail(WarehousingSaveDetailRequest warehousingSaveDetailRequest) {
        this.totalWeight = warehousingSaveDetailRequest.getTotalWeight();
        this.calculation = "Y".equals(warehousingSaveDetailRequest.getCalculationYn());
        this.remarks = warehousingSaveDetailRequest.getRemarks();
        this.modifiedDate = LocalDateTime.now();
    }
}
