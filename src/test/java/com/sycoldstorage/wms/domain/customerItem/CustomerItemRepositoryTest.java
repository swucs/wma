package com.sycoldstorage.wms.domain.customerItem;

import com.sycoldstorage.wms.adapter.presentation.web.customerItem.CustomerItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.customerItem.SearchCustomerItemCondition;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.domain.customer.Customer;
import com.sycoldstorage.wms.domain.customer.CustomerRepository;
import com.sycoldstorage.wms.domain.item.Item;
import com.sycoldstorage.wms.domain.item.ItemRepository;
import com.sycoldstorage.wms.domain.storageFee.StorageFee;
import com.sycoldstorage.wms.domain.storageFee.StorageFeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest
class CustomerItemRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    CustomerItemRepository customerItemRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    StorageFeeRepository storageFeeRepository;

    @Test
    @DisplayName("거래처별 품목 조회")
    public void search() {

        SearchCustomerItemCondition condition = new SearchCustomerItemCondition();
        condition.setCustomerId(1l);

        List<CustomerItemDto> customerItemDtos = customerItemRepository.searchCustomerItems(condition);

        for (CustomerItemDto customerItemDto : customerItemDtos) {
            System.out.println("customerItemDto = " + customerItemDto);
        }
    }

    @Test
    @DisplayName("findBy테스트")
    public void findBy() {

        Optional<Customer> customerOptional = customerRepository.findById(1l);
        if (customerOptional.isEmpty()) {
            throw new NoSuchDataException("존재하지 않는 거래처ID 입니다.");
        }

        Optional<Item> itemOptional = itemRepository.findById(1l);
        if (itemOptional.isEmpty()) {
            throw new NoSuchDataException("존재하지 않는 품목ID 입니다.");
        }

        Optional<StorageFee> storageFeeOptional = storageFeeRepository.findById(1l);
        if (storageFeeOptional.isEmpty()) {
            throw new NoSuchDataException("존재하지 않는 보관료ID 입니다.");
        }

        CustomerItem byCustomerAndItemAndStorageFee = customerItemRepository.findByCustomerAndItem
                (customerOptional.get(), itemOptional.get());
        System.out.println("byCustomerAndItemAndStorageFee = " + byCustomerAndItemAndStorageFee);
    }

}