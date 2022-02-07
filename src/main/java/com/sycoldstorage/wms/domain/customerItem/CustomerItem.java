package com.sycoldstorage.wms.domain.customerItem;

import com.sycoldstorage.wms.domain.customer.Customer;
import com.sycoldstorage.wms.domain.item.Item;
import com.sycoldstorage.wms.domain.storageFee.StorageFee;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 거래처별 품목 Entity
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@EqualsAndHashCode(of="id")
@Entity
public class CustomerItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_fee_id")
    private StorageFee storageFee;

    /**
     * 생성자
     * @param customer
     * @param item
     * @param storageFee
     */
    public CustomerItem(Customer customer, Item item, StorageFee storageFee) {
        this.changeCustomerItem(customer, item, storageFee);
    }

    public void changeCustomerItem(Customer customer, Item item, StorageFee storageFee) {
        this.customer = customer;
        this.item = item;
        this.storageFee = storageFee;
    }
}
