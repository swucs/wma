package com.sycoldstorage.wms.domain.storageFee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageFeeRepository extends JpaRepository<StorageFee, Long>, StorageFeeRepositoryCustom {
}
