package com.sycoldstorage.wms.application.service;

import com.sycoldstorage.wms.adapter.presentation.web.storageFee.SearchStorageFeeCondition;
import com.sycoldstorage.wms.adapter.presentation.web.storageFee.StorageFeeDto;
import com.sycoldstorage.wms.adapter.presentation.web.storageFee.StorageFeeSelectBoxDto;

import java.util.List;

public interface StorageFeeService {
    List<StorageFeeDto> searchStorageFees(SearchStorageFeeCondition condition);

    StorageFeeDto createStorageFee(StorageFeeDto storageFeeDto);

    StorageFeeDto updateStorageFee(StorageFeeDto storageFeeDto);

    List<StorageFeeSelectBoxDto> getAllStorageFees();
}
