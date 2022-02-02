package com.sycoldstorage.wms.domain.warehousing;

import com.sycoldstorage.wms.adapter.presentation.web.warehousing.SearchWarehousingCondition;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDetailDto;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest
class WarehousingRepositoryTest {

    @Autowired
    WarehousingRepository warehousingRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("입출고 조회")
    public void searchWarehousings() {

        SearchWarehousingCondition condition = new SearchWarehousingCondition();
        condition.setBaseDateFrom(LocalDate.of(2022, 1, 24));
        condition.setBaseDateTo(LocalDate.of(2022, 1, 29));
        condition.setCustomerName("맛죤식품");
        condition.setItemName("돼지갈비양념");
        List<WarehousingDto> result = warehousingRepository.searchWarehousings(condition);

//        assertThat(result.size()).isEqualTo(22);

        for (WarehousingDto warehousingDto : result) {
            System.out.println("warehousingDto = " + warehousingDto);
        }
    }

    @Test
    @DisplayName("입출고 내역 조회")
    public void warehousingDetail() {

        SearchWarehousingCondition condition = new SearchWarehousingCondition();
        condition.setBaseDateFrom(LocalDate.of(2022, 1, 24));
        condition.setBaseDateTo(LocalDate.of(2022, 1, 29));
        condition.setCustomerName("맛죤식품");
        condition.setItemName("돼지갈비양념");
        List<WarehousingDto> warehousingDtos = warehousingRepository.searchWarehousings(condition);

        List<WarehousingDetailDto> warehousingDetail = warehousingRepository.findWarehousingDetails(warehousingDtos.get(0).getId());

        for (WarehousingDetailDto warehousingDetailDto : warehousingDetail) {
            System.out.println("warehousingDetailDto = " + warehousingDetailDto);
        }

    }

}