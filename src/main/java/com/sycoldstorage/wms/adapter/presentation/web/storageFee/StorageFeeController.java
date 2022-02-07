package com.sycoldstorage.wms.adapter.presentation.web.storageFee;

import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.application.service.StorageFeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
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
@RequiredArgsConstructor
@RestController
public class StorageFeeController {
    private final StorageFeeService storageFeeService;
    private final StorageFeeSaveValidator storageFeeSaveValidator;

    /**
     * 보관료 목록 조회
     * @param condition
     * @return
     */
    @GetMapping("/storageFees")
    public ResponseEntity searchStorageFees(@ModelAttribute SearchStorageFeeCondition condition) {
        List<StorageFeeDto> storageFeeDtos = storageFeeService.searchStorageFees(condition);

        List<EntityModel<StorageFeeDto>> entityModels = storageFeeDtos.stream()
                .map(entity ->
                        EntityModel.of(entity
                                , linkTo(methodOn(StorageFeeController.class).searchStorageFees(null)).slash(entity.getId()).withSelfRel()
                        )
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(
                CollectionModel.of(entityModels
                        , linkTo(methodOn(StorageFeeController.class).searchStorageFees(null)).withSelfRel()
                )
                .add(Link.of("/docs/index.html#resources-storageFee-list").withRel("profile"))
        );
    }

    /**
     * 보관료 생성
     * @param request
     * @param errors
     * @return
     */
    @PostMapping("/storageFee")
    public ResponseEntity createStorageFee(@RequestBody StorageFeeDto request, Errors errors) {

        storageFeeSaveValidator.valid(request, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        StorageFeeDto createdStorageFeeDto = storageFeeService.createStorageFee(request);

        EntityModel<StorageFeeDto> entityModel = EntityModel.of(createdStorageFeeDto)
                .add(linkTo(methodOn(StorageFeeController.class).createStorageFee(null, null)).withSelfRel())
                .add(getListLink())
                .add(Link.of("/docs/index.html#resources-storageFee-create").withRel("profile"));

        //등록 후 목록 조회 URI를 반환
        URI createdUri = linkTo(methodOn(StorageFeeController.class).searchStorageFees(null)).toUri();

        return ResponseEntity.created(createdUri).body(entityModel);
    }

    /**
     * 보관료 수정
     * @param request
     * @param errors
     * @return
     */
    @PutMapping("/storageFee/{id}")
    public ResponseEntity updateStorageFee(@PathVariable Long id, @RequestBody StorageFeeDto request, Errors errors) {

        storageFeeSaveValidator.valid(request, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        StorageFeeDto savedStorageFee;
        try {
            savedStorageFee = storageFeeService.updateStorageFee(request);
        } catch (NoSuchDataException e) {
            //데이터가 없는 경우
            return ResponseEntity.notFound().build();
        }

        EntityModel<StorageFeeDto> entityModel = EntityModel.of(savedStorageFee)
                .add(linkTo(methodOn(StorageFeeController.class).createStorageFee(null, null)).withSelfRel())
                .add(getListLink())
                .add(Link.of("/docs/index.html#resources-storageFee-update").withRel("profile"));

        return ResponseEntity.ok().body(entityModel);
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
        return linkTo(methodOn(StorageFeeController.class).searchStorageFees(null)).withRel("list");
    }

    /**
     * 보관료 목록(selectbox용)
     * @return
     */
    @GetMapping("/storageFee/allCodes")
    public ResponseEntity storageFeeAllCodes() {
        List<StorageFeeSelectBoxDto> allStorageFees = storageFeeService.getAllStorageFees();
        return ResponseEntity.ok().body(allStorageFees);
    }
}
