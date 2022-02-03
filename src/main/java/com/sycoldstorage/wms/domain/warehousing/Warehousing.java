package com.sycoldstorage.wms.domain.warehousing;

import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingSaveDetailRequest;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingSaveRequest;
import com.sycoldstorage.wms.domain.customer.Customer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 입출고 Entity
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@Entity
public class Warehousing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate baseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WarehousingType warehousingType;

    @Column(nullable = false)
    private boolean quickFrozen;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "warehousing", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<WarehousingDetail> warehousingDetails = new ArrayList<>();

    /**
     * 생성자
     * @param warehousingSaveRequest
     * @param customer
     */
    public Warehousing(WarehousingSaveRequest warehousingSaveRequest, Customer customer) {
        this.baseDate = warehousingSaveRequest.getBaseDate();
        this.customer = customer;
        this.name = warehousingSaveRequest.getName();
        this.warehousingType = WarehousingType.valueOf(warehousingSaveRequest.getWarehousingTypeValue());
        this.quickFrozen = "Y".equals(warehousingSaveRequest.getQuickFrozenYn());
    }
    /**
     * 입출고 정보 수정
     * @param warehousingSaveRequest
     */
    public void changeWarehousing(WarehousingSaveRequest warehousingSaveRequest) {

        this.name = warehousingSaveRequest.getName();
        this.warehousingType = WarehousingType.valueOf(warehousingSaveRequest.getWarehousingTypeValue());
        this.quickFrozen = "Y".equals(warehousingSaveRequest.getQuickFrozenYn());

        //입출고 내역 수정 및 삭제
        this.changeWarehousingDetails(warehousingSaveRequest);
    }


    /**
     * 입출고 내역 수정 및 삭제
     * @param warehousingSaveRequest
     */
    private void changeWarehousingDetails(WarehousingSaveRequest warehousingSaveRequest) {
        //입출고 내역 수정
        List<WarehousingSaveDetailRequest> requestDetails = warehousingSaveRequest.getWarehousingDetails();
        List<WarehousingDetail> copiedWarehousingDetails = new ArrayList<>(this.warehousingDetails);    //ConcurrentModificationException를 피하기 위해 새로운 ArrayList
        for (WarehousingDetail warehousingDetail : copiedWarehousingDetails) {

            if (warehousingDetail.getId() == null) {
                //신규인 경우 SKIP
                continue;
            }

            //동일한 ID찾기
            Optional<WarehousingSaveDetailRequest> requestDetailOptional = requestDetails.stream()
                    .filter(e -> e.getId().equals(warehousingDetail.getId()))
                    .findFirst();

            if (requestDetailOptional.isPresent()) {
                //수정
                warehousingDetail.changeWarehousingDetail(requestDetailOptional.get());
//                requestDetails.remove(requestDetailOptional.get()); //수정완료한 Request는 삭제
            } else {
                //삭제
                this.warehousingDetails.remove(warehousingDetail);
            }
        }
    }

    /**
     * 입출고 내역 신규생성
     * @param warehousingDetail
     */
    public void addWarehousingDetails(WarehousingDetail warehousingDetail) {
        this.warehousingDetails.add(warehousingDetail);
    }
}
