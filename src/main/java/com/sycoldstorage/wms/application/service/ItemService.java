package com.sycoldstorage.wms.application.service;

import com.sycoldstorage.wms.adapter.presentation.web.item.ItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.ItemSelectBoxDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.SearchItemCondition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemService {

    List<ItemDto> searchItems(SearchItemCondition params);

    ItemDto createItem(ItemDto itemDto);

    ItemDto updateItem(ItemDto itemDto);

    List<ItemSelectBoxDto> getItemsByCustomerId(long customerId);

    @Transactional
    void deleteItem(long id);
}
