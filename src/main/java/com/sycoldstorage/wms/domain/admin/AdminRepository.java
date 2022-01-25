package com.sycoldstorage.wms.domain.admin;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 어드민 Repository
 */
public interface AdminRepository extends JpaRepository<Admin, String> {
}
