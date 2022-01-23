package com.sycoldstorage.wms.repository;

import com.sycoldstorage.wms.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 어드민 Repository
 */
public interface AdminRepository extends JpaRepository<Admin, String> {
}
