package com.sycoldstorage.wms.adapter.presentation.web.customer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;

/**
 * 거래처 신규 생성 Validator
 */
@Component
public class CustomerCreateValidator {

//    private final static String ENCODING_UTF_8 = "UTF-8";
    public void valid(CustomerDto customerDto, Errors errors) {

        String name = customerDto.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "notEmpty", "이름을 입력하세요.");
        } else {
            if (name.length() > 100) {
                errors.rejectValue("name", "overSize", "이름을 100자 이하로 입력하세요.");
            }
        }

        String businessNumber = customerDto.getBusinessNumber();
        if (StringUtils.isEmpty(businessNumber)) {
            errors.rejectValue("businessNumber", "notEmpty", "사업자등록번호를 입력하세요.");
        } else {
            if (businessNumber.length() > 12) {
                errors.rejectValue("businessNumber", "overSize", "사업자등록번호를 12자 이하로 입력하세요.");
            }

            if (!Pattern.matches("^(\\d{3})+[-]+(\\d{2})+[-]+(\\d{5})", businessNumber)) {
                errors.rejectValue("businessNumber", "pattern", "사업자등록번호 형식이 맞지 않습니다. [000-00-00000]");
            }
        }

        String representativeName = customerDto.getRepresentativeName();
        if (StringUtils.isEmpty(representativeName)) {
            errors.rejectValue("representativeName", "notEmpty", "대표자명을 입력하세요.");
        } else {
            if (representativeName.length() > 20) {
                errors.rejectValue("representativeName", "overSize", "대표자명을 20자 이하로 입력하세요.");
            }
        }

        //업태
        String businessConditions = customerDto.getBusinessConditions();
        if (StringUtils.length(businessConditions) > 50) {
            errors.rejectValue("businessConditions", "overSize", "업태를 50자 이하로 입력하세요.");
        }

        //업종
        String typeOfBusiness = customerDto.getTypeOfBusiness();
        if (StringUtils.length(typeOfBusiness) > 50) {
            errors.rejectValue("typeOfBusiness", "overSize", "업종을 50자 이하로 입력하세요.");
        }

        //주소
        String address = customerDto.getAddress();
        if (StringUtils.isEmpty(address)) {
            errors.rejectValue("address", "notEmpty", "주소를 입력하세요.");
        } else {
            if (address.length() > 200) {
                errors.rejectValue("address", "overSize", "주소를 200자 이하로 입력하세요.");
            }
        }

        //전화번호
        String phoneNumber = customerDto.getPhoneNumber();
        if (StringUtils.isEmpty(phoneNumber)) {
            errors.rejectValue("phoneNumber", "notEmpty", "전화번호를 입력하세요.");
        } else {
            if (phoneNumber.length() > 13) {
                errors.rejectValue("phoneNumber", "overSize", "전화번호를 13자 이하로 입력하세요.");
            }

            if (!Pattern.matches("^(\\d{2,3})+[-]+(\\d{3,4})+[-]+(\\d{4})", phoneNumber)) {
                errors.rejectValue("phoneNumber", "pattern", "전화번호 형식이 맞지 않습니다. [000-000-0000]");
            }
        }

        //Fax번호
        String faxNumber = customerDto.getFaxNumber();
        if (StringUtils.length(faxNumber) > 13) {
            errors.rejectValue("faxNumber", "overSize", "FAX번호를 13자 이하로 입력하세요.");
        }
        if (!Pattern.matches("^(\\d{2,3})+[-]+(\\d{3,4})+[-]+(\\d{4})", faxNumber)) {
            errors.rejectValue("faxNumber", "pattern", "FAX번호 형식이 맞지 않습니다. [000-000-0000]");
        }


        //사용여부
        String useYn = customerDto.getUseYn();
        if (StringUtils.isEmpty(useYn)) {
            errors.rejectValue("useYn", "notEmpty", "사용여부를 입력하세요.");
        } else {
            if (!("Y".equals(useYn) || "N".equals(useYn))) {
                errors.rejectValue("useYn", "wrongValue", "사용여부는 Y 또는 N를 입력해야 합니다.");
            }
        }
    }

    public static void main(String[] args) {
            System.out.println(Pattern.matches("^(\\d{3})+[-]+(\\d{2})+[-]+(\\d{5})", "000-00-00000"));
    }
}
