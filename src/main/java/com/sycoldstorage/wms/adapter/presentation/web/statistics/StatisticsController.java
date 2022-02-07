package com.sycoldstorage.wms.adapter.presentation.web.statistics;


import com.sycoldstorage.wms.application.service.WarehousingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final WarehousingService warehousingService;

    /**
     * 고개 품목별 기간변 통계 목록
     * @param condition
     * @return
     */
    @GetMapping("/customerItemTermStatistics")
    public ResponseEntity searchCustomerItemTermStatistics(
                                            @ModelAttribute @Validated SearchCustomerItemTermCondition condition
                                            , Errors errors) {

        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(methodOn(StatisticsController.class).searchCustomerItemTermStatistics(null, null));

        if (errors.hasErrors()) {
            return badRequest(errors, webMvcLinkBuilder.withRel("list"));
        }

        List<CustomerItemTermDto> customerItemTermDtos = warehousingService.searchCustomerItemTermStatistics(condition);


        return ResponseEntity.ok().body(
                                CollectionModel.of(customerItemTermDtos
                                    , webMvcLinkBuilder.withSelfRel()
                                )
                                .add(Link.of("/docs/index.html#resources-customer-item-term-list").withRel("profile"))
        );
    }


    /**
     * 에러 발생시 badRequest처리
     * @param errors
     * @return
     */
    private ResponseEntity badRequest(Errors errors, Link link) {
        EntityModel<Errors> entityModel = EntityModel.of(errors);
        entityModel.add(link);  //error를 그냥 던지는것이 아니고 index 링크 추가한다.
        return ResponseEntity.badRequest().body(entityModel);
    }

}
