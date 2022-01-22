package com.sycoldstorage.wms.repository;

import com.sycoldstorage.wms.dto.CustomerDto;
import com.sycoldstorage.wms.dto.SearchCustomerCondition;
import com.sycoldstorage.wms.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @PersistenceContext
    EntityManager em;

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
        List<CustomerDto> result = customerRepository.searchCustomers(condition);

        assertThat(result.size()).isEqualTo(22);

        for (CustomerDto customer : result) {
            System.out.println("customer = " + customer);
        }
    }

    @Test
    @DisplayName("거래처 수정")
    void update() {
        Optional<Customer> byId = customerRepository.findById(2l);

        assertThat(byId.isPresent()).isTrue();
        assertThat(byId.get().getName()).isEqualTo("거래처1");

        CustomerDto dto = CustomerDto.builder()
                .name("거래처 수정")
                .businessNumber("123458888")
                .representativeName("홍길동 수정")
                .businessConditions("도매,제조 수정")
                .typeOfBusiness("축산업,양념육 수정")
                .address("경기도 포천시 소흘읍 392-25 수정")
                .phoneNumber("031-541-9999")
                .faxNumber("031-531-9999")
                .use(true)
                .build();

        byId.get().changeCustomer(dto);

        em.flush();
        em.clear();


        Optional<Customer> updated = customerRepository.findById(byId.get().getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get().getName()).isEqualTo("거래처 수정");

        log.info("updated = {}" + updated.get());

    }

    @Test
    @DisplayName("거래처 삭제")
    void delete() {
        Optional<Customer> byId = customerRepository.findById(2l);

        assertThat(byId.isPresent()).isTrue();
        assertThat(byId.get().getName()).isEqualTo("거래처1");

        byId.get().remove();

        em.flush();
        em.clear();


        Optional<Customer> deleted = customerRepository.findById(byId.get().getId());

        assertThat(deleted.isPresent()).isTrue();
        assertThat(deleted.get().isDeleted()).isTrue();

        log.info("updated = {}" + deleted.get());

    }


}
