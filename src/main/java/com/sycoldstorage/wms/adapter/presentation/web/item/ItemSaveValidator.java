package com.sycoldstorage.wms.adapter.presentation.web.item;

import com.sycoldstorage.wms.adapter.presentation.web.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ItemSaveValidator implements Validator<ItemDto> {

    @Override
    public void valid(ItemDto itemDto, Errors errors) {
        String name = itemDto.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "notEmpty", "품목명을 입력하세요.");
        } else {
            if (name.length() > 100) {
                errors.rejectValue("name", "overSize", "이름을 100자 이하로 입력하세요.");
            }
        }

        Double unitWeight = itemDto.getUnitWeight();
        if (unitWeight == null) {
            errors.rejectValue("unitWeight", "notEmpty", "단위무게를 입력하세요.");
        }

        String unitName = itemDto.getUnitName();
        if (StringUtils.isEmpty(unitName)) {
            errors.rejectValue("unitName", "notEmpty", "단위명을 입력하세요.");
        } else {
            if (unitName.length() > 100) {
                errors.rejectValue("unitName", "overSize", "단위명을 100자 이하로 입력하세요.");
            }
        }

        String remarks = itemDto.getRemarks();
        if (StringUtils.isNotEmpty(remarks)) {
            if (remarks.length() > 200) {
                errors.rejectValue("remarks", "overSize", "비고를 200자 이하로 입력하세요.");
            }
        }
    }
}
