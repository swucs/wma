package com.sycoldstorage.wms.domain.warehousing;

import com.sycoldstorage.wms.adapter.presentation.web.statistics.CustomerItemTermDto;
import com.sycoldstorage.wms.adapter.presentation.web.statistics.SearchCustomerItemTermCondition;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.SearchWarehousingCondition;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDetailDto;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDto;

import java.util.List;

public interface WarehousingCustom {
    List<WarehousingDto> searchWarehousings(SearchWarehousingCondition condition);

    WarehousingDto findWarehousingById(long id);

    List<WarehousingDetailDto> findWarehousingDetails(Long warehousingId);

    List<CustomerItemTermDto> findCustomerItemTermStatisticsList(SearchCustomerItemTermCondition condition);
}
