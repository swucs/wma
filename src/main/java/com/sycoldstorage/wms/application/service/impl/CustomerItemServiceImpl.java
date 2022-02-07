package com.sycoldstorage.wms.application.service.impl;

import com.sycoldstorage.wms.adapter.presentation.web.customerItem.CustomerItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.customerItem.CustomerItemSaveRequestDto;
import com.sycoldstorage.wms.adapter.presentation.web.customerItem.SearchCustomerItemCondition;
import com.sycoldstorage.wms.application.exception.DuplicatedDataException;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.application.service.CustomerItemService;
import com.sycoldstorage.wms.domain.customer.Customer;
import com.sycoldstorage.wms.domain.customer.CustomerRepository;
import com.sycoldstorage.wms.domain.customerItem.CustomerItem;
import com.sycoldstorage.wms.domain.customerItem.CustomerItemRepository;
import com.sycoldstorage.wms.domain.item.Item;
import com.sycoldstorage.wms.domain.item.ItemRepository;
import com.sycoldstorage.wms.domain.storageFee.StorageFee;
import com.sycoldstorage.wms.domain.storageFee.StorageFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerItemServiceImpl implements CustomerItemService {

    private final CustomerItemRepository customerItemRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final StorageFeeRepository storageFeeRepository;

    /**
     * 거래처별 품목 조회
     * @param condition
     * @return
     */
    @Override
    public List<CustomerItemDto> search(SearchCustomerItemCondition condition) {
        return customerItemRepository.searchCustomerItems(condition);
    }

    /**
     * 거래처별 품목 신규생성
     * @param request
     * @return
     */
    @Transactional
    @Override
    public CustomerItemDto create(CustomerItemSaveRequestDto request) {

        Customer customer = this.getCustomer(request.getCustomerId());
        Item item = this.getItem(request.getItemId());
        StorageFee storageFee = this.getStorageFee(request.getStorageFeeId());

        //중복체크
        CustomerItem existedCustomItem = customerItemRepository.findByCustomerAndItem(customer, item);
        if (existedCustomItem != null) {
            throw new DuplicatedDataException("이미 등록된 거래처/품목 정보가 존재합니다.");
        }

        //Insert
        CustomerItem newCustomerItem = new CustomerItem(customer, item, storageFee);
        CustomerItem savedCustomerItem = customerItemRepository.save(newCustomerItem);

        return customerItemRepository.findDtoById(savedCustomerItem.getId());
    }


    /**
     * 거래처별 품목 수정
     * @param request
     * @return
     */
    @Transactional
    @Override
    public CustomerItemDto update(CustomerItemSaveRequestDto request) {

        CustomerItem savedCustomerItem = customerItemRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchDataException("존재하지 않는 거래처별 품목정보입니다."));


        Customer customer = this.getCustomer(request.getCustomerId());
        Item item = this.getItem(request.getItemId());
        StorageFee storageFee = this.getStorageFee(request.getStorageFeeId());

        //중복체크
        CustomerItem existedCustomItem = customerItemRepository.findByCustomerAndItem(customer, item);
        if (existedCustomItem != null && existedCustomItem.getId() != request.getId()) {
            throw new DuplicatedDataException("이미 등록된 거래처/품목 정보가 존재합니다.");
        }

        //Update
        savedCustomerItem.changeCustomerItem(customer, item, storageFee);

        return customerItemRepository.findDtoById(savedCustomerItem.getId());
    }

    private Customer getCustomer(long cusomterId) {
        return customerRepository.findById(cusomterId).orElseThrow(() -> new NoSuchDataException("존재하지 않는 거래처ID 입니다."));
    }

    private Item getItem(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NoSuchDataException("존재하지 않는 품목ID 입니다."));
    }

    private StorageFee getStorageFee(long storageFeeId) {
        return storageFeeRepository.findById(storageFeeId).orElseThrow(() -> new NoSuchDataException("존재하지 않는 보관료ID 입니다."));
    }

    /**
     * 거래처별 품목 삭제
     * @param id
     */
    @Transactional
    @Override
    public void delete(long id) {
        CustomerItem savedCustomerItem = customerItemRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataException("존재하지 않는 거래처별 품목정보입니다."));

        customerItemRepository.delete(savedCustomerItem);
    }

}
