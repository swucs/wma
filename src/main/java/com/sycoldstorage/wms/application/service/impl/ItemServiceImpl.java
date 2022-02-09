package com.sycoldstorage.wms.application.service.impl;

import com.sycoldstorage.wms.adapter.presentation.web.item.ItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.ItemSelectBoxDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.SearchItemCondition;
import com.sycoldstorage.wms.application.exception.ForeignKeyConstraintException;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.application.service.ItemService;
import com.sycoldstorage.wms.domain.customerItem.CustomerItem;
import com.sycoldstorage.wms.domain.customerItem.CustomerItemRepository;
import com.sycoldstorage.wms.domain.item.Item;
import com.sycoldstorage.wms.domain.item.ItemRepository;
import com.sycoldstorage.wms.domain.warehousing.Warehousing;
import com.sycoldstorage.wms.domain.warehousing.WarehousingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 품목관리 Service
 */
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CustomerItemRepository customerItemRepository;
    private final WarehousingRepository warehousingRepository;

    @Override
    public List<ItemDto> searchItems(SearchItemCondition condition) {
        return itemRepository.searchItems(condition);
    }

    @Transactional
    @Override
    public ItemDto createItem(ItemDto itemDto) {
        Item item = new Item(itemDto);
        return itemRepository.save(item).toItemDto();
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        Item savedItem = findItem(itemDto.getId());
        savedItem.changeItem(itemDto);
        return savedItem.toItemDto();
    }

    private Item findItem(long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NoSuchDataException("존재하지 않는 품목입니다."));
    }

    /**
     * 해당 거래처의 품목목록
     * @return
     */
    @Override
    public List<ItemSelectBoxDto> getItemsByCustomerId(long customerId) {
        return itemRepository.findItemsByCustomerId(customerId);
    }


    @Transactional
    @Override
    public void deleteItem(long id) {
        Item savedItem = findItem(id);

        //거래처별 품목정보 체크
        checkExistingCustomerItem(savedItem);

        //입출고 정보 체크
        checkExistingWarehousing(id);

        itemRepository.delete(savedItem);
    }

    /**
     * 거래처별 품목정보 체크
     * @param savedItem
     */
    private void checkExistingCustomerItem(Item savedItem) {
        List<CustomerItem> customerItems = customerItemRepository.findCustomerItemsByItem(savedItem);
        if (!CollectionUtils.isEmpty(customerItems)) {
            String customerNames = customerItems.stream().map(e -> e.getCustomer().getName()).collect(Collectors.joining(", "));
            throw new ForeignKeyConstraintException("해당 품목의 거래처별 품목 정보가 존재하여 삭제할 수 없습니다. [거래처명 : " + customerNames + "]");
        }
    }

    /**
     * 입출고 정보 체크
     * @param id
     */
    private void checkExistingWarehousing(long id) {
        List<Warehousing> warehousings = warehousingRepository.findWarehousingDetails(id);
        if (!CollectionUtils.isEmpty(warehousings)) {
            String customerNames = warehousings.stream().map(e -> e.getCustomer().getName()).collect(Collectors.joining(", "));
            throw new ForeignKeyConstraintException("입출고 내역이 존재하여 삭제할 수 없습니다. [거래처명 : " + customerNames + "]");
        }
    }
}
