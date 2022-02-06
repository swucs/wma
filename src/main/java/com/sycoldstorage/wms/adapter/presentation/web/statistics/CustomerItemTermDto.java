package com.sycoldstorage.wms.adapter.presentation.web.statistics;

import lombok.Data;

import java.time.LocalDate;

/**
 * 고객 품목별 기간별 통계
 */
@Data
public class CustomerItemTermDto {
    private String itemName;
    private Double incomingUnitQty;
    private Double incomingWeightQty;
    private Double outgoingUnitQty;
    private Double outgoingWeightQty;
    private Double incomingStockWeightQty;
    private Double outgoingStockWeightQty;
    private String unitName;
    private Double unitWeight;
    private LocalDate recentBaseDate;

    public CustomerItemTermDto(String itemName, Double incomingUnitQty, Double incomingWeightQty, Double outgoingUnitQty
            , Double outgoingWeightQty, Double incomingStockWeightQty, Double outgoingStockWeightQty, String unitName
            , Double unitWeight, LocalDate recentBaseDate) {
        this.itemName = itemName;
        this.incomingUnitQty = incomingUnitQty;
        this.incomingWeightQty = incomingWeightQty;
        this.outgoingUnitQty = outgoingUnitQty;
        this.outgoingWeightQty = outgoingWeightQty;
        this.incomingStockWeightQty = incomingStockWeightQty;
        this.outgoingStockWeightQty = outgoingStockWeightQty;
        this.unitName = unitName;
        this.unitWeight = unitWeight;
        this.recentBaseDate = recentBaseDate;
    }

    /**
     * 입고량
     * @return
     */
    public String getIncomingQtyText() {
        return new StringBuffer()
                .append(Math.floor(incomingUnitQty / unitWeight))
                .append(unitName)
                .append(" ")
                .append(incomingUnitQty % unitWeight)
                .append("kg")
                .toString();
    }

    /**
     * 출고량
     * @return
     */
    public String getOutgoingQtyText() {
        return new StringBuffer()
                .append(Math.floor(outgoingUnitQty / unitWeight))
                .append(unitName)
                .append(" ")
                .append(outgoingUnitQty % unitWeight)
                .append("kg")
                .toString();
    }

    /**
     * 재고량
     * @return
     */
    public String getStockQtyText() {

        double totalStock = incomingStockWeightQty - outgoingStockWeightQty;

        return new StringBuffer()
                .append(Math.floor(totalStock / unitWeight))
                .append(unitName)
                .append(" ")
                .append(totalStock % unitWeight)
                .append("kg")
                .toString();
    }
}
