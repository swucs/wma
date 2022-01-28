package com.sycoldstorage.wms.application.service.impl;

import com.sycoldstorage.wms.adapter.presentation.web.item.ItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.SearchItemCondition;
import com.sycoldstorage.wms.application.service.ItemService;
import com.sycoldstorage.wms.domain.item.Item;
import com.sycoldstorage.wms.domain.item.ItemRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    ItemRepository itemRepository;

    ItemService itemService;

    @BeforeEach
    void setup() {
        itemService = new ItemServiceImpl(itemRepository);
    }

    @Test
    @DisplayName("품목 검색")
    void searchItems() {
        //given
        SearchItemCondition condition = new SearchItemCondition();
        condition.setName("품목");

        List<ItemDto> itemDtos = Arrays.asList(
                ItemDto.builder().id(1l).name("품목1").unitName("단위명1").unitWeight(15.0).remarks("비고1").build()
                , ItemDto.builder().id(2l).name("품목2").unitName("단위명2").unitWeight(15.0).remarks("비고2").build()
                , ItemDto.builder().id(3l).name("품목3").unitName("단위명3").unitWeight(15.0).remarks("비고3").build()
                , ItemDto.builder().id(4l).name("품목4").unitName("단위명4").unitWeight(15.0).remarks("비고4").build()
                , ItemDto.builder().id(5l).name("품목5").unitName("단위명5").unitWeight(15.0).remarks("비고5").build()
        );
        when(itemRepository.searchItems(condition)).thenReturn(itemDtos);

        List<ItemDto> result = itemService.searchItems(condition);
        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(1)).extracting("name").isEqualTo("품목2");

        for (ItemDto itemDto : result) {
            System.out.println("itemDto = " + itemDto);
        }
    }

    @Test
    @DisplayName("품목 생성")
    void create() {

        ItemDto dto = Item.builder()
                .name("신규 품목")
                .unitName("단위명")
                .unitWeight(15.0)
                .remarks("비고1")
                .build()
                .toItemDto();

        when(itemRepository.save(any(Item.class))).then(AdditionalAnswers.returnsFirstArg());

        ItemDto created = itemService.createItem(dto);

        assertThat(created.getName()).isEqualTo(dto.getName());
        assertThat(created.getUnitName()).isEqualTo(dto.getUnitName());
        assertThat(created.getUnitWeight()).isEqualTo(dto.getUnitWeight());
        assertThat(created.getRemarks()).isEqualTo(dto.getRemarks());

        log.info("created : {}", created);

    }

    @Test
    @DisplayName("품목 수정")
    void update() {

        final long id = 1l;

        ItemDto itemDto = ItemDto.builder()
                .id(id)
                .name("신규 품목")
                .unitName("단위명")
                .unitWeight(15.0)
                .remarks("비고1")
                .build();

        log.info("itemDto : {}", itemDto);

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(new Item(itemDto)));


        ItemDto updated = itemService.updateItem(itemDto);
        assertThat(updated.getName()).isEqualTo(itemDto.getName());
        assertThat(updated.getUnitName()).isEqualTo(itemDto.getUnitName());
        assertThat(updated.getUnitWeight()).isEqualTo(itemDto.getUnitWeight());
        assertThat(updated.getRemarks()).isEqualTo(itemDto.getRemarks());

        log.info("updated : {}", updated);
    }

}