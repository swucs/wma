package com.sycoldstorage.wms.service;

import com.sycoldstorage.wms.dto.CustomerDto;
import com.sycoldstorage.wms.dto.SearchCustomerCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<CustomerDto> searchCustomers(SearchCustomerCondition condition, Pageable pageable);
}
