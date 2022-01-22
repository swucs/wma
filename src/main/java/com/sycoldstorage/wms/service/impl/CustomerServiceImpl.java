package com.sycoldstorage.wms.service.impl;

import com.sycoldstorage.wms.dto.CustomerDto;
import com.sycoldstorage.wms.dto.SearchCustomerCondition;
import com.sycoldstorage.wms.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public class CustomerServiceImpl implements CustomerService {


    /**
     * 거래처목록 검색
     * @param : 검색조건을 담은 객체
     * @return
     */
    @Override
    public Page<CustomerDto> searchCustomers(SearchCustomerCondition condition, Pageable pageable) {
        return null;



//        //Page, sort 정보를 생성
//        PageRequest pageRequest = PageRequest.of(params.getPage(), params.getPageSize(), Sort.by("id"));
//        //검색조건 생성
//        Specification<Customer> specification = CustomerSpecification.searchWith(params);
//        return customerRepository.findAll(specification, pageRequest);
    }

}
