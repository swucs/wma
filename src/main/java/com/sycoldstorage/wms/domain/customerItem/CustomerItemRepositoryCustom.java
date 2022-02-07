package com.sycoldstorage.wms.domain.customerItem;

import com.sycoldstorage.wms.adapter.presentation.web.customerItem.CustomerItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.customerItem.SearchCustomerItemCondition;

import java.util.List;

public interface CustomerItemRepositoryCustom {
    List<CustomerItemDto> searchCustomerItems(SearchCustomerItemCondition condition);

    CustomerItemDto findDtoById(long id);
}
