package com.sycoldstorage.wms.adapter.presentation.web.customer;


import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.application.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerSaveValidator customerSaveValidator;

    /**
     * 거래처 목록
     * @param condition
     * @return
     */
    @GetMapping("/customers")
    public ResponseEntity searchCustomers(@ModelAttribute SearchCustomerCondition condition) {

        List<CustomerDto> customerDtos = customerService.searchCustomers(condition);

        List<EntityModel<CustomerDto>> entityModels = customerDtos.stream()
                .map(entity ->
                                EntityModel.of(entity
                                        , linkTo(methodOn(CustomerController.class).searchCustomers(null)).slash(entity.getId()).withSelfRel()
                                )
                )
                .collect(Collectors.toList());


        log.info("entityModels : {}", entityModels);
        log.info("entityModels.size : {}", entityModels.size());

        return ResponseEntity.ok().body(
                                CollectionModel.of(entityModels
                                    , linkTo(methodOn(CustomerController.class).searchCustomers(null)).withSelfRel()
                                )
                                .add(Link.of("/docs/index.html#resources-customers-list").withRel("profile"))
        );
    }

    /**
     * 거래처정보 등록
     * @param request
     * @param errors
     * @return
     */
    @PostMapping("/customer")
    public ResponseEntity createCustomer(@RequestBody @Validated CustomerDto request,
                                         Errors errors) {

        customerSaveValidator.valid(request, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        CustomerDto createdCustomerDto = customerService.createCustomer(request);

        EntityModel<CustomerDto> entityModel = EntityModel.of(createdCustomerDto)
                .add(linkTo(methodOn(CustomerController.class).createCustomer(null, null)).withSelfRel())
                .add(getListLink())
                .add(Link.of("/docs/index.html#resources-customer-create").withRel("profile"));

        //등록 후 목록 조회 URI를 반환
        URI createdUri = linkTo(methodOn(CustomerController.class).searchCustomers(null)).toUri();

        return ResponseEntity.created(createdUri).body(entityModel);
    }


    /**
     * 거래처정보 수정
     * @param id
     * @param request
     * @param errors
     * @return
     */
    @PutMapping("/customer/{id}")
    public ResponseEntity updateCustomer(@PathVariable Long id,
                                         @RequestBody @Validated CustomerDto request,
                                         Errors errors) {

        customerSaveValidator.valid(request, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        CustomerDto updatedCustomerDto = null;
        try {
            updatedCustomerDto = customerService.updateCustomer(request);
        } catch (NoSuchDataException e) {
            //데이터가 없는 경우
            return ResponseEntity.notFound().build();
        }

        EntityModel<CustomerDto> entityModel = EntityModel.of(updatedCustomerDto)
                .add(linkTo(methodOn(CustomerController.class).updateCustomer(id, null, null)).withSelfRel())
                .add(getListLink())
                .add(Link.of("/docs/index.html#resources-customer-update").withRel("profile"));

        return ResponseEntity.ok(entityModel);

    }


    /**
     * 거래처정보 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/customer/{id}")
    public ResponseEntity deleteCustomer(@PathVariable long id) {

        try {
            customerService.deleteCustomer(id);
        } catch (NoSuchDataException e) {
            //데이터가 없는 경우
            return ResponseEntity.notFound().build();
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
        return linkTo(methodOn(CustomerController.class).searchCustomers(null)).withRel("list");
    }
}
