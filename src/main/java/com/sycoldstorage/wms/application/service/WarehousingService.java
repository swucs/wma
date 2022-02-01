package com.sycoldstorage.wms.application.service;

import com.sycoldstorage.wms.adapter.presentation.web.warehousing.SearchWarehousingCondition;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDetailDto;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDto;

import java.util.List;

public interface WarehousingService {
    List<WarehousingDto> searchWarehousings(SearchWarehousingCondition condition);

    List<WarehousingDetailDto> getWarehousingDetail(long warehousingId);
}
