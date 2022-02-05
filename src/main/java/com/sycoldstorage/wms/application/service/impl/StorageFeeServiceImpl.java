package com.sycoldstorage.wms.application.service.impl;

import com.sycoldstorage.wms.adapter.presentation.web.storageFee.SearchStorageFeeCondition;
import com.sycoldstorage.wms.adapter.presentation.web.storageFee.StorageFeeDto;
import com.sycoldstorage.wms.application.exception.NoSuchDataException;
import com.sycoldstorage.wms.application.service.StorageFeeService;
import com.sycoldstorage.wms.domain.storageFee.StorageFee;
import com.sycoldstorage.wms.domain.storageFee.StorageFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StorageFeeServiceImpl implements StorageFeeService {

    private final StorageFeeRepository storageFeeRepository;

    /**
     * 보관료 목록
     * @param condition
     * @return
     */
    @Override
    public List<StorageFeeDto> searchStorageFees(SearchStorageFeeCondition condition) {
        return storageFeeRepository.searchStorageFees(condition);
    }

    /**
     * 보관료 생성
     * @param storageFeeDto
     * @return
     */
    @Transactional
    @Override
    public StorageFeeDto createStorageFee(StorageFeeDto storageFeeDto) {
        StorageFee storageFee = new StorageFee(storageFeeDto);
        return storageFeeRepository.save(storageFee).toStorageFeeDto();
    }

    /**
     * 보관료 수정
     * @param storageFeeDto
     * @return
     */
    @Transactional
    @Override
    public StorageFeeDto updateStorageFee(StorageFeeDto storageFeeDto) {

        Long id = storageFeeDto.getId();
        Optional<StorageFee> storageFeeOptional = storageFeeRepository.findById(id);

        if (storageFeeOptional.isEmpty()) {
            throw new NoSuchDataException();
        }

        StorageFee storageFee = storageFeeOptional.get();
        storageFee.changeStorageFee(storageFeeDto);
        return storageFee.toStorageFeeDto();
    }
}
