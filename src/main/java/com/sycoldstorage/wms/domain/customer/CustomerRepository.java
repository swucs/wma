package com.sycoldstorage.wms.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 거래처 Repository
 */
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
}
