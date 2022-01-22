package com.sycoldstorage.wms.repository;

import com.sycoldstorage.wms.dto.CustomerDto;
import com.sycoldstorage.wms.dto.SearchCustomerCondition;

import java.util.List;

public interface CustomerRepositoryCustom {
    List<CustomerDto> searchCustomers(SearchCustomerCondition condition);
}
