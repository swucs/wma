package com.sycoldstorage.wms.adapter.presentation.web.warehousing;

import com.sycoldstorage.wms.application.service.WarehousingService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * 입출고 Controller
 */

@RequiredArgsConstructor
@RestController
public class WarehousingController {
    private final WarehousingService warehousingService;

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
}
