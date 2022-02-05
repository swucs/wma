package com.sycoldstorage.wms.application.service.impl;

import com.sycoldstorage.wms.adapter.presentation.web.storageFee.SearchStorageFeeCondition;
import com.sycoldstorage.wms.adapter.presentation.web.storageFee.StorageFeeDto;
import com.sycoldstorage.wms.application.service.StorageFeeService;
import com.sycoldstorage.wms.domain.storageFee.StorageFee;
import com.sycoldstorage.wms.domain.storageFee.StorageFeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest
class StorageFeeServiceImplTest {

    @Autowired
    private StorageFeeRepository storageFeeRepository;

    private StorageFeeService storageFeeService;

    @BeforeEach
    public void setup() {

        storageFeeService = new StorageFeeServiceImpl(storageFeeRepository);

        StorageFee storageFee;
        LocalDate baseDate = LocalDate.of(2000, 1, 1);

        for (int i = 0; i < 10; i++) {
            storageFee = StorageFee.builder()
                    .baseDate(baseDate)
                    .name("군납기본" + ( i % 3))
                    .storage(1.25 + (i * 0.01))
                    .loading(20 + i)
                    .build();

            storageFeeRepository.save(storageFee);
        }
    }

    @Test
    @DisplayName("보관료 검색")
    void search() {
        //given
        SearchStorageFeeCondition condition = new SearchStorageFeeCondition();
        condition.setName("군납기본2");



        List<StorageFeeDto> result = storageFeeService.searchStorageFees(condition);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(1)).extracting("name").isEqualTo("군납기본1");

        for (StorageFeeDto storageFeeDto : result) {
            System.out.println("storageFeeDto = " + storageFeeDto);
        }
    }

    @Test
    @DisplayName("보관료 생성")
    @Transactional
    void create() {
        //given
        StorageFeeDto storageFeeDto = StorageFeeDto.builder()
                .baseDate(LocalDate.now())
                .name("보관료")
                .storage(1.35)
                .loading(20d)
                .build();

        StorageFeeDto saveStorageFeeDto = storageFeeService.createStorageFee(storageFeeDto);
        assertThat(saveStorageFeeDto.getBaseDate()).isEqualTo(storageFeeDto.getBaseDate());
        assertThat(saveStorageFeeDto.getName()).isEqualTo(storageFeeDto.getName());
        assertThat(saveStorageFeeDto.getStorage()).isEqualTo(storageFeeDto.getStorage());
        assertThat(saveStorageFeeDto.getLoading()).isEqualTo(storageFeeDto.getLoading());
    }

    @Test
    @DisplayName("보관료 수정")
    @Transactional
    void update() {
        //given
        StorageFeeDto storageFeeDto = storageFeeRepository.findById(1l).get().toStorageFeeDto();

        storageFeeDto.setBaseDate(LocalDate.of(2000, 1, 1));
        storageFeeDto.setName("보관료 수정");
        storageFeeDto.setStorage(1.25);
        storageFeeDto.setLoading(30d);

        StorageFeeDto saveStorageFeeDto = storageFeeService.updateStorageFee(storageFeeDto);

        assertThat(saveStorageFeeDto.getBaseDate()).isEqualTo(storageFeeDto.getBaseDate());
        assertThat(saveStorageFeeDto.getName()).isEqualTo(storageFeeDto.getName());
        assertThat(saveStorageFeeDto.getStorage()).isEqualTo(storageFeeDto.getStorage());
        assertThat(saveStorageFeeDto.getLoading()).isEqualTo(storageFeeDto.getLoading());
    }
}