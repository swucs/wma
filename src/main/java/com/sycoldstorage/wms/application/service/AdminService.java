package com.sycoldstorage.wms.application.service;

import com.sycoldstorage.wms.domain.admin.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminService extends UserDetailsService {
    Admin createAdmin(Admin admin);

    Admin getAdminById(String id);
}
