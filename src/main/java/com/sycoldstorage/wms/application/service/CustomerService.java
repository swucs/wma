package com.sycoldstorage.wms.application.service;

import com.sycoldstorage.wms.adapter.presentation.web.customer.CustomerDto;
import com.sycoldstorage.wms.adapter.presentation.web.customer.CustomerSelectBoxDto;
import com.sycoldstorage.wms.adapter.presentation.web.customer.SearchCustomerCondition;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface CustomerService {
    List<CustomerDto> searchCustomers(SearchCustomerCondition condition);

    @Transactional
    CustomerDto createCustomer(CustomerDto customerDto);

    @Transactional
    CustomerDto updateCustomer(CustomerDto customerDto) throws NoSuchDataException;

    @Transactional
    void deleteCustomer(Long id) throws NoSuchDataException;

    List<CustomerSelectBoxDto> getValidCustomers();
}
