package com.sycoldstorage.wms.service.impl;

import com.sycoldstorage.wms.dto.CustomerDto;
import com.sycoldstorage.wms.dto.SearchCustomerCondition;
import com.sycoldstorage.wms.entity.Customer;
import com.sycoldstorage.wms.repository.CustomerRepository;
import com.sycoldstorage.wms.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    CustomerService customerService;


    @BeforeEach
    void setup() {
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    @DisplayName("거래처 검색")
    void searchCustomers() {
        //given
        SearchCustomerCondition condition = new SearchCustomerCondition();
        condition.setName("거래처");
        condition.setUseYn("Y");

        List<CustomerDto> customerDtos = Arrays.asList(
                CustomerDto.builder().id(1l).name("거래처1").address("주소1").businessConditions("업종1").faxNumber("팩스1").use(true).build()
                , CustomerDto.builder().id(2l).name("거래처2").address("주소2").businessConditions("업종2").faxNumber("팩스2").use(true).build()
                , CustomerDto.builder().id(3l).name("거래처3").address("주소3").businessConditions("업종3").faxNumber("팩스3").use(true).build()
                , CustomerDto.builder().id(4l).name("거래처4").address("주소4").businessConditions("업종4").faxNumber("팩스4").use(true).build()
                , CustomerDto.builder().id(5l).name("거래처5").address("주소5").businessConditions("업종5").faxNumber("팩스5").use(true).build()
        );
        when(customerRepository.searchCustomers(condition)).thenReturn(customerDtos);

        List<CustomerDto> result = customerService.searchCustomers(condition);
        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(1)).extracting("name").isEqualTo("거래처2");

        for (CustomerDto customerDto : result) {
            System.out.println("customerDto = " + customerDto);
        }
    }

    @Test
    @DisplayName("거래처 생성")
    void create() {

        CustomerDto dto = Customer.builder()
                .name("신규 거래처")
                .businessNumber("1234567890")
                .representativeName("홍길동")
                .businessConditions("도매,제조")
                .typeOfBusiness("축산업,양념육")
                .address("경기도 포천시 소흘읍 이동교리 392-25")
                .phoneNumber("031-541-9960")
                .faxNumber("031-531-7186")
                .use(true)
                .build()
                .toCustomerDto();

        when(customerRepository.save(any(Customer.class))).then(AdditionalAnswers.returnsFirstArg());

        Customer created = customerService.create(dto);

        assertThat(created.getName()).isEqualTo(dto.getName());
        assertThat(created.getBusinessNumber()).isEqualTo(dto.getBusinessNumber());
        assertThat(created.getRepresentativeName()).isEqualTo(dto.getRepresentativeName());
        assertThat(created.getBusinessConditions()).isEqualTo(dto.getBusinessConditions());
        assertThat(created.getTypeOfBusiness()).isEqualTo(dto.getTypeOfBusiness());
        assertThat(created.getAddress()).isEqualTo(dto.getAddress());
        assertThat(created.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        assertThat(created.getFaxNumber()).isEqualTo(dto.getFaxNumber());
        assertThat(created.isUse()).isEqualTo(dto.isUse());

        log.info("created : {}", created);

    }

    @Test
    @DisplayName("거래처 수정")
    void update() {

        final long id = 1l;

        Customer customer = Customer.builder()
                .id(id)
                .name("신규 거래처")
                .businessNumber("1234567890")
                .representativeName("홍길동")
                .businessConditions("도매,제조")
                .typeOfBusiness("축산업,양념육")
                .address("경기도 포천시 소흘읍 이동교리 392-25")
                .phoneNumber("031-541-9960")
                .faxNumber("031-531-7186")
                .use(true)
                .build();

        log.info("customer : {}", customer);

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));


        Customer updated = customerService.update(customer.toCustomerDto());
        assertThat(updated.getName()).isEqualTo(customer.getName());
        assertThat(updated.getBusinessNumber()).isEqualTo(customer.getBusinessNumber());
        assertThat(updated.getRepresentativeName()).isEqualTo(customer.getRepresentativeName());
        assertThat(updated.getBusinessConditions()).isEqualTo(customer.getBusinessConditions());
        assertThat(updated.getTypeOfBusiness()).isEqualTo(customer.getTypeOfBusiness());
        assertThat(updated.getAddress()).isEqualTo(customer.getAddress());
        assertThat(updated.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        assertThat(updated.getFaxNumber()).isEqualTo(customer.getFaxNumber());
        assertThat(updated.isUse()).isEqualTo(customer.isUse());

        log.info("updated : {}", updated);
    }

    @Test
    @DisplayName("거래처 삭제처리")
    void delete() {
        final long id = 3l;

        Customer customer = Customer.builder()
                .id(id)
                .name("신규 거래처")
                .businessNumber("1234567890")
                .representativeName("홍길동")
                .businessConditions("도매,제조")
                .typeOfBusiness("축산업,양념육")
                .address("경기도 포천시 소흘읍 이동교리 392-25")
                .phoneNumber("031-541-9960")
                .faxNumber("031-531-7186")
                .use(true)
                .build();

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        Customer deleted = customerService.delete(id);

        assertThat(deleted.getName()).isEqualTo(customer.getName());
        assertThat(deleted.getBusinessNumber()).isEqualTo(customer.getBusinessNumber());
        assertThat(deleted.getRepresentativeName()).isEqualTo(customer.getRepresentativeName());
        assertThat(deleted.getBusinessConditions()).isEqualTo(customer.getBusinessConditions());
        assertThat(deleted.getTypeOfBusiness()).isEqualTo(customer.getTypeOfBusiness());
        assertThat(deleted.getAddress()).isEqualTo(customer.getAddress());
        assertThat(deleted.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        assertThat(deleted.getFaxNumber()).isEqualTo(customer.getFaxNumber());
        assertThat(deleted.isUse()).isEqualTo(customer.isUse());

        assertThat(deleted.isDeleted()).isTrue();
    }
}