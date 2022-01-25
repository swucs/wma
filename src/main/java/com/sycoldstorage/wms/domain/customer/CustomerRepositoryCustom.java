package com.sycoldstorage.wms.domain.customer;

import com.sycoldstorage.wms.adapter.presentation.web.customer.CustomerDto;
import com.sycoldstorage.wms.adapter.presentation.web.customer.SearchCustomerCondition;

import java.util.List;


/**
 * QueryDsl을 위한 interface
 */
public interface CustomerRepositoryCustom {
    List<CustomerDto> searchCustomers(SearchCustomerCondition condition);
}
