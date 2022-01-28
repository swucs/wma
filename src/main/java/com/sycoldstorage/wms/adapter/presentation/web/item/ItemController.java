package com.sycoldstorage.wms.adapter.presentation.web.item;

import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.application.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemSaveValidator itemSaveValidator;

    /**
     * 품목 목록
     * @param condition
     * @return
     */
    @GetMapping("/items")
    public ResponseEntity searchItems(@ModelAttribute SearchItemCondition condition) {

        List<ItemDto> itemDtos = itemService.searchItems(condition);

        List<EntityModel<ItemDto>> entityModels = itemDtos.stream()
                .map(entity ->
                        EntityModel.of(entity
                                , linkTo(methodOn(ItemController.class).searchItems(null)).slash(entity.getId()).withSelfRel()
                        )
                )
                .collect(Collectors.toList());


        log.info("entityModels : {}", entityModels);
        log.info("entityModels.size : {}", entityModels.size());

        return ResponseEntity.ok().body(
                CollectionModel.of(entityModels
                                , linkTo(methodOn(ItemController.class).searchItems(null)).withSelfRel()
                        )
                        .add(Link.of("/docs/index.html#resources-items-list").withRel("profile"))
        );
    }

    /**
     * 거래처정보 등록
     * @param request
     * @param errors
     * @return
     */
    @PostMapping("/item")
    public ResponseEntity createItem(@RequestBody @Validated ItemDto request,
                                         Errors errors) {

        itemSaveValidator.valid(request, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        ItemDto createdItemDto = itemService.createItem(request);

        EntityModel<ItemDto> entityModel = EntityModel.of(createdItemDto)
                .add(linkTo(methodOn(ItemController.class).createItem(null, null)).withSelfRel())
                .add(getListLink())
                .add(Link.of("/docs/index.html#resources-item-create").withRel("profile"));

        //등록 후 목록 조회 URI를 반환
        URI createdUri = linkTo(methodOn(ItemController.class).searchItems(null)).toUri();

        return ResponseEntity.created(createdUri).body(entityModel);
    }


    /**
     * 거래처정보 등록
     * @param request
     * @param errors
     * @return
     */
    @PutMapping("/item/{id}")
    public ResponseEntity updateItem(@PathVariable Long id,
                                     @RequestBody @Validated ItemDto request,
                                     Errors errors) {

        itemSaveValidator.valid(request, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        ItemDto updatedItemDto = null;
        try {
            updatedItemDto = itemService.updateItem(request);
        } catch (NoSuchDataException e) {
            //데이터가 없는 경우
            return ResponseEntity.notFound().build();
        }

        EntityModel<ItemDto> entityModel = EntityModel.of(updatedItemDto)
                .add(linkTo(methodOn(ItemController.class).updateItem(id, null, null)).withSelfRel())
                .add(getListLink())
                .add(Link.of("/docs/index.html#resources-item-update").withRel("profile"));

        return ResponseEntity.ok(entityModel);
    }

    /**
     * 에러 발생시 badRequest처리
     * @param errors
     * @return
     */
    private ResponseEntity badRequest(Errors errors) {
        EntityModel<Errors> entityModel = EntityModel.of(errors);
        entityModel.add(getListLink());  //error를 그냥 던지는것이 아니고 index 링크 추가한다.
        return ResponseEntity.badRequest().body(entityModel);
    }


    /**
     * list 링크
     * @return
     */
    private Link getListLink() {
        return linkTo(methodOn(ItemController.class).searchItems(null)).withRel("list");
    }
}
