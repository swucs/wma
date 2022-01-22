package com.sycoldstorage.wms.repository;

import com.sycoldstorage.wms.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 거래처 Repository
 */
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
}
