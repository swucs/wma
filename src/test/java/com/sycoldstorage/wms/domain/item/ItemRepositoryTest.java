package com.sycoldstorage.wms.domain.item;

import com.sycoldstorage.wms.adapter.presentation.web.item.ItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.SearchItemCondition;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void setup() {
        Item item;
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 22; i++) {
            item = Item.builder()
                    .name("품목" + i)
                    .unitWeight(15.0)
                    .unitName("상자")
                    .remarks("군납")
                    .registerdDate(now)
                    .build();

            itemRepository.save(item);
        }

    }

    @Test
    @DisplayName("품목 조회")
    public void searchItem() {

        SearchItemCondition condition = new SearchItemCondition();
        condition.setName("품목");
        List<ItemDto> result = itemRepository.searchItems(condition);

        assertThat(result.size()).isEqualTo(22);

        for (ItemDto item : result) {
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("품목 수정")
    void update() {
        Optional<Item> byId = itemRepository.findById(2l);

        assertThat(byId.isPresent()).isTrue();
        assertThat(byId.get().getName()).isEqualTo("품목1");

        ItemDto dto = ItemDto.builder()
                .name("품목 수정")
                .unitWeight(16.0)
                .unitName("박스")
                .remarks("학교납품")
                .build();

        byId.get().changeItem(dto);

        em.flush();
        em.clear();


        Optional<Item> updated = itemRepository.findById(byId.get().getId());

        assertThat(updated.isPresent()).isTrue();
        assertThat(updated.get().getName()).isEqualTo("품목 수정");

        log.info("updated = {}" + updated.get());

    }

}