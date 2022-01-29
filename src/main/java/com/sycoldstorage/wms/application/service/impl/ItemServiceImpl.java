package com.sycoldstorage.wms.application.service.impl;

import com.sycoldstorage.wms.adapter.presentation.web.item.ItemDto;
import com.sycoldstorage.wms.adapter.presentation.web.item.SearchItemCondition;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.application.service.ItemService;
import com.sycoldstorage.wms.domain.item.Item;
import com.sycoldstorage.wms.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 품목관리 Service
 */
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> searchItems(SearchItemCondition condition) {
        return itemRepository.searchItems(condition);
    }

    @Transactional
    @Override
    public ItemDto createItem(ItemDto itemDto) {
        Item item = new Item(itemDto);
        return itemRepository.save(item).toItemDto();
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        Long id = itemDto.getId();

        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            item.changeItem(itemDto);
            return item.toItemDto();
        } else {
            throw new NoSuchDataException();
        }
    }
}
