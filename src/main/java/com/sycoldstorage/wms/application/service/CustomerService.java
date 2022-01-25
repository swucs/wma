package com.sycoldstorage.wms.application.service;

import com.sycoldstorage.wms.adapter.presentation.web.customer.CustomerDto;
import com.sycoldstorage.wms.adapter.presentation.web.customer.SearchCustomerCondition;
import com.sycoldstorage.wms.domain.customer.Customer;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;

import javax.transaction.Transactional;
import java.util.List;

public interface CustomerService {
    List<CustomerDto> searchCustomers(SearchCustomerCondition condition);

    @Transactional
    Customer create(CustomerDto customerDto);

    @Transactional
    Customer update(CustomerDto customerDto) throws NoSuchDataException;

    @Transactional
    Customer delete(Long id) throws NoSuchDataException;
}
