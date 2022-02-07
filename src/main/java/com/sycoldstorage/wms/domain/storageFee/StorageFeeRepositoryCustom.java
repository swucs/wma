package com.sycoldstorage.wms.domain.storageFee;

import com.sycoldstorage.wms.adapter.presentation.web.storageFee.SearchStorageFeeCondition;
import com.sycoldstorage.wms.adapter.presentation.web.storageFee.StorageFeeDto;
import com.sycoldstorage.wms.adapter.presentation.web.storageFee.StorageFeeSelectBoxDto;

import java.util.List;

public interface StorageFeeRepositoryCustom {
    List<StorageFeeDto> searchStorageFees(SearchStorageFeeCondition condition);

    List<StorageFeeSelectBoxDto> findAllStorageFees();
}
