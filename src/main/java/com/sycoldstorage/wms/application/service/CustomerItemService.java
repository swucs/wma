package com.sycoldstorage.wms.application.service;

import com.sycoldstorage.wms.adapter.presentation.web.customerItem.CustomerItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.customerItem.CustomerItemSaveRequestDto;
import com.sycoldstorage.wms.adapter.presentation.web.customerItem.SearchCustomerItemCondition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerItemService {
    List<CustomerItemDto> search(SearchCustomerItemCondition condition);

    CustomerItemDto create(CustomerItemSaveRequestDto request);

    @Transactional
    CustomerItemDto update(CustomerItemSaveRequestDto request);

    @Transactional
    void delete(long id);
}
