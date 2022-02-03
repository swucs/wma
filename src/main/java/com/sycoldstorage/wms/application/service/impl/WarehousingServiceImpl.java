package com.sycoldstorage.wms.application.service.impl;

import com.sycoldstorage.wms.adapter.presentation.web.warehousing.SearchWarehousingCondition;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDetailDto;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingDto;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingSaveDetailRequest;
import com.sycoldstorage.wms.adapter.presentation.web.warehousing.WarehousingSaveRequest;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.application.service.WarehousingService;
import com.sycoldstorage.wms.domain.item.Item;
import com.sycoldstorage.wms.domain.item.ItemRepository;
import com.sycoldstorage.wms.domain.warehousing.Warehousing;
import com.sycoldstorage.wms.domain.warehousing.WarehousingDetail;
import com.sycoldstorage.wms.domain.warehousing.WarehousingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WarehousingServiceImpl implements WarehousingService {

    private final WarehousingRepository warehousingRepository;
    private final ItemRepository itemRepository;


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
    public List<WarehousingDetailDto> getWarehousingDetails(long warehousingId) {
        return warehousingRepository.findWarehousingDetails(warehousingId);
    }


    /**
     * 입출고 수정
     * @param warehousingSaveRequest
     * @return
     * @throws NoSuchDataException
     */
    @Transactional
    @Override
    public WarehousingDto updateWarehousing(WarehousingSaveRequest warehousingSaveRequest) throws NoSuchDataException {

        Long warehousingId = warehousingSaveRequest.getId();
        Optional<Warehousing> warehousingOptional = warehousingRepository.findById(warehousingId);

        if (warehousingOptional.isPresent()) {
            Warehousing warehousing = warehousingOptional.get();
            warehousing.changeWarehousing(warehousingSaveRequest);  //입출고 및 내역 변경내용 저장

            //입출고 내역 신규
            List<WarehousingSaveDetailRequest> requestDetails = warehousingSaveRequest.getWarehousingDetails();
            for (WarehousingSaveDetailRequest requestDetail : requestDetails) {
                //신규
                if (requestDetail.getId() == null) {
                    Item item = itemRepository.findById(requestDetail.getItemId()).orElseThrow(() -> new NoSuchDataException());
                    warehousing.addWarehousingDetails(new WarehousingDetail(warehousing, item, requestDetail));
                }
            }

        } else {
            throw new NoSuchDataException();
        }

        return warehousingRepository.findWarehousingById(warehousingId);
    }
}
