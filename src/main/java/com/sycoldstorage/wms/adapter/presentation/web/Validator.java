package com.sycoldstorage.wms.adapter.presentation.web;

import org.springframework.validation.Errors;

/**
 * 유효성 검증
 */
public interface Validator<T> {
    void valid(T dto, Errors errors);
}
