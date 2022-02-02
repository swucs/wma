package com.sycoldstorage.wms.application.service;

import com.sycoldstorage.wms.adapter.presentation.web.warehousing.SearchWarehousingCondition;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDetailDto;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDto;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingSaveRequest;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;

import javax.transaction.Transactional;
import java.util.List;

public interface WarehousingService {
    List<WarehousingDto> searchWarehousings(SearchWarehousingCondition condition);

    List<WarehousingDetailDto> getWarehousingDetails(long warehousingId);

    @Transactional
    void updateWarehousing(WarehousingSaveRequest warehousingSaveRequest) throws NoSuchDataException;
}
