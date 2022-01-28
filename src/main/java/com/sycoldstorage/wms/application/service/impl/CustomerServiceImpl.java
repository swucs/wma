package com.sycoldstorage.wms.application.service.impl;

import com.sycoldstorage.wms.adapter.presentation.web.customer.CustomerDto;
import com.sycoldstorage.wms.adapter.presentation.web.customer.SearchCustomerCondition;
import com.sycoldstorage.wms.domain.customer.Customer;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.domain.customer.CustomerRepository;
import com.sycoldstorage.wms.application.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * 거래처목록 검색
     * @param : 검색조건을 담은 객체
     * @return
     */
    @Override
    public List<CustomerDto> searchCustomers(SearchCustomerCondition condition) {
        return customerRepository.searchCustomers(condition);
    }

    /**
     * 거래처정보 생성
     * @param customerDto
     * @return
     */
    @Transactional
    @Override
    public CustomerDto create(CustomerDto customerDto) {

        Customer newCustomer = new Customer(customerDto);
        return customerRepository.save(newCustomer).toCustomerDto();
    }

    /**
     * 거래처정보 수정
     * @param customerDto
     * @return
     * @throws NoSuchDataException
     */
    @Transactional
    @Override
    public CustomerDto update(CustomerDto customerDto) throws NoSuchDataException {

        Long id = customerDto.getId();

        Optional<Customer> customerOptional = customerRepository.findById(id);

        if (customerOptional.isPresent()) {
            //request값을 customer로 매핑
            Customer customer = customerOptional.get();
            customer.changeCustomer(customerDto);
            return customer.toCustomerDto();

        } else {
            throw new NoSuchDataException();
        }
    }


    /**
     * 거래처정보 삭제 (플래그 처리)
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public CustomerDto delete(Long id) throws NoSuchDataException {

        Optional<Customer> customerOptional = customerRepository.findById(id);

        if (customerOptional.isPresent()) {
            //request값을 customer로 매핑
            Customer customer = customerOptional.get();
            customer.remove();
            return customer.toCustomerDto();

        } else {
            throw new NoSuchDataException();
        }
    }

}
