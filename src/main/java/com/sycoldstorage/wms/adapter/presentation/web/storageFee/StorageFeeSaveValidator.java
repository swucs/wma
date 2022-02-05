package com.sycoldstorage.wms.adapter.presentation.web.storageFee;

import com.sycoldstorage.wms.adapter.presentation.web.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDate;

@Component
public class StorageFeeSaveValidator implements Validator<StorageFeeDto> {

    @Override
    public void valid(StorageFeeDto dto, Errors errors) {

        LocalDate baseDate = dto.getBaseDate();
        if (baseDate == null) {
            errors.rejectValue("baseDate", "notEmpty", "기준일자를 입력하세요.");
        }

        String name = dto.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "notEmpty", "보관료명을 입력하세요.");
        } else {
            if (name.length() > 100) {
                errors.rejectValue("name", "overSize", "보관료명은 100자 이하로 입력하세요.");
            }
        }

        Double storage = dto.getStorage();
        if (storage == null) {
            errors.rejectValue("storage", "notEmpty", "보관료를 입력하세요.");
        } else {
            if (storage <= 0) {
                errors.rejectValue("storage", "minValue", "보관료는 0보다 큰 금액이어야 합니다.");
            }
        }

        Double loading = dto.getLoading();
        if (loading == null) {
            errors.rejectValue("loading", "notEmpty", "상하차비를 입력하세요.");
        } else {
            if (loading <= 0) {
                errors.rejectValue("loading", "minValue", "상하차비는 0보다 큰 금액이어야 합니다.");
            }
        }
    }
}
