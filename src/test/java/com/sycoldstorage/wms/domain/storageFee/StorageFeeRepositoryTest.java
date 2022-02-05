package com.sycoldstorage.wms.domain.storageFee;

import com.sycoldstorage.wms.adapter.presentation.web.storageFee.SearchStorageFeeCondition;
import com.sycoldstorage.wms.adapter.presentation.web.storageFee.StorageFeeDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest
class StorageFeeRepositoryTest {

    @Autowired
    private StorageFeeRepository storageFeeRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void setup() {
        StorageFee storageFee;
        LocalDate baseDate = LocalDate.of(2000, 1, 1);

        for (int i = 0; i < 10; i++) {
            storageFee = StorageFee.builder()
                    .baseDate(baseDate)
                    .name("군납기본" + i)
                    .storage(1.25 + (i * 0.01))
                    .loading(20 + i)
                    .build();

            storageFeeRepository.save(storageFee);
        }
    }

    @Test
    @DisplayName("보관료 조회")
    public void searchStorageFee() {

        SearchStorageFeeCondition condition = new SearchStorageFeeCondition();
        condition.setName("군납기본");
        List<StorageFeeDto> result = storageFeeRepository.searchStorageFees(condition);

        assertThat(result.size()).isEqualTo(10);

        for (StorageFeeDto storageFeeDto : result) {
            System.out.println("storageFeeDto = " + storageFeeDto);
        }
    }
}