package com.sycoldstorage.wms.service;

import com.sycoldstorage.wms.dto.CustomerDto;
import com.sycoldstorage.wms.dto.SearchCustomerCondition;
import com.sycoldstorage.wms.entity.Customer;
import com.sycoldstorage.wms.exception.NoSuchDataException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
