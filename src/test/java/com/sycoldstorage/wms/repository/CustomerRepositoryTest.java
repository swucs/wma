package com.sycoldstorage.wms.repository;

import com.sycoldstorage.wms.dto.CustomerDto;
import com.sycoldstorage.wms.dto.SearchCustomerCondition;
import com.sycoldstorage.wms.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    public void setup() {
        Customer customer;

        for (int i = 0; i < 22; i++) {
            customer = Customer.builder()
                    .name("거래처" + i)
                    .businessNumber("1234567890")
                    .representativeName("홍길동" + i)
                    .businessConditions("도매,제조")
                    .typeOfBusiness("축산업,양념육")
                    .address("경기도 포천시 소흘읍 이동교리 392-25")
                    .phoneNumber("031-541-9960")
                    .faxNumber("031-531-7186")
                    .use(true)
                    .deleted(false)
                    .build();

            customerRepository.save(customer);
        }

    }


    @Test
    @DisplayName("거래처 페이징 조회")
    public void searchCustomer() {

        SearchCustomerCondition condition = new SearchCustomerCondition();
        condition.setName("거래처");
        condition.setUseYn("Y");
        List<CustomerDto> result = customerRepository.searchPageSimple(condition);

        assertThat(result.size()).isEqualTo(22);

        for (CustomerDto customer : result) {
            System.out.println("customer = " + customer);
        }

    }


}
