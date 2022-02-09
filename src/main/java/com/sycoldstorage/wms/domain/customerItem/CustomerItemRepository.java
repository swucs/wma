package com.sycoldstorage.wms.domain.customerItem;

import com.sycoldstorage.wms.domain.customer.Customer;
import com.sycoldstorage.wms.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerItemRepository extends JpaRepository<CustomerItem, Long>, CustomerItemRepositoryCustom {
    CustomerItem findByCustomerAndItem(Customer customer, Item item);
    List<CustomerItem> findCustomerItemsByItem(Item item);
}
