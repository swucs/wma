package com.sycoldstorage.wms.domain.warehousing;

import com.sycoldstorage.wms.adapter.presentation.web.warehousing.SearchWarehousingCondition;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDto;

import java.util.List;

public interface WarehousingCustom {
    List<WarehousingDto> searchWarehousings(SearchWarehousingCondition condition);
}
