package com.sycoldstorage.wms.domain.warehousing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehousingRepository extends JpaRepository<Warehousing, Long>, WarehousingCustom {
//    List<WarehousingDetail> findByCustomer(Customer customer);
}
