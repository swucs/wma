package com.sycoldstorage.wms.application.service.impl;

import com.sycoldstorage.wms.adapter.presentation.web.warehousing.SearchWarehousingCondition;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDetailDto;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDto;
import com.sycoldstorage.wms.application.service.WarehousingService;
import com.sycoldstorage.wms.domain.warehousing.WarehousingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WarehousingServiceImpl implements WarehousingService {

    private final WarehousingRepository warehousingRepository;


    /**
     * 입출고 목록
     * @param condition
     * @return
     */
    @Override
    public List<WarehousingDto> searchWarehousings(SearchWarehousingCondition condition) {
        return warehousingRepository.searchWarehousings(condition);
    }

    /**
     * 입출고 상세내역 목록
     * @param warehousingId
     * @return
     */
    @Override
    public List<WarehousingDetailDto> getWarehousingDetail(long warehousingId) {
        return warehousingRepository.findWarehousingDetail(warehousingId);
    }
}
