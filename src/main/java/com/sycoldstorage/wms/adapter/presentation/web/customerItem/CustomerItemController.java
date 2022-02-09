package com.sycoldstorage.wms.adapter.presentation.web.customerItem;


import com.sycoldstorage.wms.adapter.presentation.web.customer.CustomerDto;
import com.sycoldstorage.wms.application.exception.DuplicatedDataException;
import com.sycoldstorage.wms.application.exception.ForeignKeyConstraintException;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.application.service.CustomerItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class CustomerItemController {

    private final CustomerItemService customerItemService;
    private final CustomerItemSaveValidator customerItemSaveValidator;

    /**
     * 거래처별품목 목록
     * @param condition
     * @return
     */
    @GetMapping("/customerItems")
    public ResponseEntity searchCustomerItems(@ModelAttribute SearchCustomerItemCondition condition) {

        List<CustomerItemDto> customerItemDtos = customerItemService.search(condition);

        List<EntityModel<CustomerItemDto>> entityModels = customerItemDtos.stream()
                .map(entity ->
                                EntityModel.of(entity
                                        , linkTo(methodOn(CustomerItemController.class).searchCustomerItems(null)).slash(entity.getId()).withSelfRel()
                                )
                )
                .collect(Collectors.toList());


        log.info("entityModels : {}", entityModels);
        log.info("entityModels.size : {}", entityModels.size());

        return ResponseEntity.ok().body(
                                CollectionModel.of(entityModels
                                    , linkTo(methodOn(CustomerItemController.class).searchCustomerItems(null)).withSelfRel()
                                )
                                .add(Link.of("/docs/index.html#resources-customer-items-list").withRel("profile"))
        );
    }

    /**
     * 거래처별품목정보 등록
     * @param request
     * @param errors
     * @return
     */
    @PostMapping("/customerItem")
    public ResponseEntity createCustomerItem(@RequestBody @Validated CustomerItemSaveRequestDto request, Errors errors) {

        customerItemSaveValidator.valid(request, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        CustomerItemDto createdCustomerItemDto = null;
        try {
            createdCustomerItemDto = customerItemService.create(request);
        } catch (DuplicatedDataException e) {
            //중복된 데이터
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }

        EntityModel<CustomerItemDto> entityModel = EntityModel.of(createdCustomerItemDto)
                .add(linkTo(methodOn(CustomerItemController.class).createCustomerItem(null, null)).withSelfRel())
                .add(getListLink())
                .add(Link.of("/docs/index.html#resources-customer-item-create").withRel("profile"));

        //등록 후 목록 조회 URI를 반환
        URI createdUri = linkTo(methodOn(CustomerItemController.class).searchCustomerItems(null)).toUri();

        return ResponseEntity.created(createdUri).body(entityModel);
    }


    /**
     * 거래처별품목정보 수정
     * @param id
     * @param request
     * @param errors
     * @return
     */
    @PutMapping("/customerItem/{id}")
    public ResponseEntity updateCustomerItem(@PathVariable Long id,
                                         @RequestBody @Validated CustomerItemSaveRequestDto request,
                                         Errors errors) {

        customerItemSaveValidator.valid(request, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        CustomerItemDto updatedCustomerItemDto = null;
        try {
            updatedCustomerItemDto = customerItemService.update(request);
        } catch (DuplicatedDataException e) {
            //중복된 데이터
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }

        EntityModel<CustomerItemDto> entityModel = EntityModel.of(updatedCustomerItemDto)
                .add(linkTo(methodOn(CustomerItemController.class).updateCustomerItem(id, null, null)).withSelfRel())
                .add(getListLink())
                .add(Link.of("/docs/index.html#resources-customer-item-update").withRel("profile"));

        return ResponseEntity.ok(entityModel);

    }


    /**
     * 거래처별품목정보 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/customerItem/{id}")
    public ResponseEntity deleteCustomerItem(@PathVariable long id) {

        try {
            customerItemService.delete(id);
        } catch (NoSuchDataException e) {
            //데이터가 없는 경우
            return ResponseEntity.notFound().build();
        } catch (ForeignKeyConstraintException e) {
            //입출고내역이 존재하는 경우
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }

        EntityModel<CustomerDto> entityModel = EntityModel.of(CustomerDto.builder().id(id).build())
                .add(getListLink())
                .add(Link.of("/docs/index.html#resources-customer-delete").withRel("profile"));

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
        return linkTo(methodOn(CustomerItemController.class).searchCustomerItems(null)).withRel("list");
    }
}
