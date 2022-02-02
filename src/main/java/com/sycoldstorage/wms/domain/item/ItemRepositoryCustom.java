package com.sycoldstorage.wms.domain.item;

import com.sycoldstorage.wms.adapter.presentation.web.item.ItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.ItemSelectBoxDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.SearchItemCondition;

import java.util.List;

public interface ItemRepositoryCustom {
    List<ItemDto> searchItems(SearchItemCondition condition);

    List<ItemSelectBoxDto> findItemsByCustomerId(long customerId);
}
