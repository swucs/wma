package com.sycoldstorage.wms.controller;


import com.sycoldstorage.wms.dto.CustomerDto;
import com.sycoldstorage.wms.dto.SearchCustomerCondition;
import com.sycoldstorage.wms.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/api")
public class CustomerController {

    private final CustomerService customerService;


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

        return ResponseEntity.ok().body(
                CollectionModel.of(entityModels
                                    , linkTo(methodOn(CustomerController.class).searchCustomers(null)).withSelfRel()
                                )
                        .add(Link.of("/docs/index.html#resources-customers-list").withRel("profile"))
        );
    }
}
