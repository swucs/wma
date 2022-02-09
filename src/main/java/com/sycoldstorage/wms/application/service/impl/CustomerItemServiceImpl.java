package com.sycoldstorage.wms.application.service.impl;

import com.sycoldstorage.wms.adapter.presentation.web.customerItem.CustomerItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.customerItem.CustomerItemSaveRequestDto;
import com.sycoldstorage.wms.adapter.presentation.web.customerItem.SearchCustomerItemCondition;
import com.sycoldstorage.wms.application.exception.DuplicatedDataException;
import com.sycoldstorage.wms.application.exception.ForeignKeyConstraintException;
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
import com.sycoldstorage.wms.domain.warehousing.WarehousingDetail;
import com.sycoldstorage.wms.domain.warehousing.WarehousingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerItemServiceImpl implements CustomerItemService {

    private final CustomerItemRepository customerItemRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final StorageFeeRepository storageFeeRepository;
    private final WarehousingRepository warehousingRepository;

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
        checkDuplicatedIfCreate(customer, item);

        //Insert
        CustomerItem newCustomerItem = new CustomerItem(customer, item, storageFee);
        CustomerItem savedCustomerItem = customerItemRepository.save(newCustomerItem);

        return customerItemRepository.findDtoById(savedCustomerItem.getId());
    }


    /**
     * 생성시 중복체크
     * @param customer
     * @param item
     */
    private void checkDuplicatedIfCreate(Customer customer, Item item) {
        CustomerItem existedCustomItem = customerItemRepository.findByCustomerAndItem(customer, item);
        if (existedCustomItem != null) {
            throw new DuplicatedDataException("이미 등록된 거래처/품목 정보가 존재합니다.");
        }
    }


    /**
     * 거래처별 품목 수정
     * @param request
     * @return
     */
    @Transactional
    @Override
    public CustomerItemDto update(CustomerItemSaveRequestDto request) {

        CustomerItem savedCustomerItem = getCustomerItem(request.getId());
        Customer customer = this.getCustomer(request.getCustomerId());
        Item item = this.getItem(request.getItemId());
        StorageFee storageFee = this.getStorageFee(request.getStorageFeeId());

        //중복체크
        checkDuplicatedIfUpdate(request, customer, item);

        //Update
        savedCustomerItem.changeCustomerItem(customer, item, storageFee);

        return customerItemRepository.findDtoById(savedCustomerItem.getId());
    }

    /**
     * CUSTOMER_ITEM 조회
     * @param id
     * @return
     */
    private CustomerItem getCustomerItem(long id) {
        return customerItemRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataException("존재하지 않는 거래처별 품목정보입니다."));
    }

    /**
     * 수정시 중복체크
     * @param request
     * @param customer
     * @param item
     */
    private void checkDuplicatedIfUpdate(CustomerItemSaveRequestDto request, Customer customer, Item item) {
        CustomerItem existedCustomItem = customerItemRepository.findByCustomerAndItem(customer, item);
        if (existedCustomItem != null && existedCustomItem.getId() != request.getId()) {
            throw new DuplicatedDataException("이미 등록된 거래처/품목 정보가 존재합니다.");
        }
    }

    private Customer getCustomer(long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new NoSuchDataException("존재하지 않는 거래처ID 입니다."));
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
        CustomerItem savedCustomerItem = getCustomerItem(id);

        //입출고 내역
        Long customerId = savedCustomerItem.getCustomer().getId();
        Long itemId = savedCustomerItem.getItem().getId();

        //입출고내역 체크
        checkExistingWarehousingDetail(customerId, itemId);

        customerItemRepository.delete(savedCustomerItem);
    }

    /**
     * 입출고내역 체크
     * @param customerId
     * @param itemId
     */
    private void checkExistingWarehousingDetail(Long customerId, Long itemId) {
        List<WarehousingDetail> warehousingDetails = warehousingRepository.findWarehousingDetails(customerId, itemId);
        if (!CollectionUtils.isEmpty(warehousingDetails)) {
            throw new ForeignKeyConstraintException("입출고 내역이 존재하여 삭제할 수 없습니다.");
        }
    }

}
