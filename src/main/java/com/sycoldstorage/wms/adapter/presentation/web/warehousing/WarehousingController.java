package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import com.sycoldstorage.wms.adapter.presentation.web.customer.CustomerController;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.application.service.WarehousingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * 입출고 Controller
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class WarehousingController {
    private final WarehousingService warehousingService;

    /**
     * 입출고 목록
     * @param condition
     * @return
     */
    @GetMapping("/warehousings")
    public ResponseEntity searchWarehousings(@ModelAttribute SearchWarehousingCondition condition) {

        List<WarehousingDto> warehousingDtos = warehousingService.searchWarehousings(condition);

        List<EntityModel<WarehousingDto>> entityModels = warehousingDtos.stream()
                .map(entity ->
                        EntityModel.of(entity
                                , linkTo(methodOn(WarehousingController.class).searchWarehousings(null)).slash(entity.getId()).withSelfRel()
                        )
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(
                CollectionModel.of(entityModels
                        , linkTo(methodOn(WarehousingController.class).searchWarehousings(null)).withSelfRel()
                )
                .add(Link.of("/docs/index.html#resources-warehousing-list").withRel("profile"))
        );

    }

    /**
     * 입출고상세의 입출고내역 목록
     * @param id
     * @return
     */
    @GetMapping("/warehousing/{id}/details")
    public ResponseEntity warehousingDetails(@PathVariable Long id) {

        List<WarehousingDetailDto> warehousingDetails = warehousingService.getWarehousingDetails(id);

        return ResponseEntity.ok().body(
                CollectionModel.of(warehousingDetails
                    , linkTo(methodOn(WarehousingController.class).warehousingDetails(id)).withSelfRel()
                )
                .add(Link.of("/docs/index.html#resources-warehousing-detail-list").withRel("profile"))
        );
    }

    @PutMapping("/warehousing/{id}")
    public ResponseEntity updateWarehousing(@PathVariable Long id
                                            , @RequestBody WarehousingSaveRequest request) {

        log.info("id : {}", id);
        log.info("request : {}", request);

        try {
            warehousingService.updateWarehousing(request);
        } catch (NoSuchDataException e) {
            //데이터가 없는 경우
            return ResponseEntity.notFound().build();
        }

        EntityModel<WarehousingDto> entityModel = EntityModel.of(WarehousingDto.builder().build())
                .add(linkTo(methodOn(CustomerController.class).updateCustomer(id, null, null)).withSelfRel())
                .add(getListLink())
                .add(Link.of("/docs/index.html#resources-customer-update").withRel("profile"));

        return ResponseEntity.ok(entityModel);
    }

    /**
     * list 링크
     * @return
     */
    private Link getListLink() {
        return linkTo(methodOn(CustomerController.class).searchCustomers(null)).withRel("list");
    }

}
