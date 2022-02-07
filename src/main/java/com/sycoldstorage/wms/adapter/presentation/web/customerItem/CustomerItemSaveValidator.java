package com.sycoldstorage.wms.adapter.presentation.web.customerItem;

import com.sycoldstorage.wms.adapter.presentation.web.Validator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;

/**
 * 거래처 신규 생성 Validator
 */
@Component
public class CustomerItemSaveValidator implements Validator<CustomerItemSaveRequestDto> {

    public void valid(CustomerItemSaveRequestDto request, Errors errors) {

        //거래처
        Long customerId = request.getCustomerId();
        if (customerId == null) {
            errors.rejectValue("customerId", "notEmpty", "거래처를 선택하세요.");
        }

        //품목
        Long itemId = request.getItemId();
        if (itemId == null) {
            errors.rejectValue("itemId", "notEmpty", "품목을 입력하세요.");
        }

        //보관료
        Long storageFeeId = request.getStorageFeeId();
        if (storageFeeId == null) {
            errors.rejectValue("storageFeeId", "notEmpty", "보관료를 선택하세요.");
        }
    }

    public static void main(String[] args) {
            System.out.println(Pattern.matches("^(\\d{3})+[-]+(\\d{2})+[-]+(\\d{5})", "000-00-00000"));
    }
}
