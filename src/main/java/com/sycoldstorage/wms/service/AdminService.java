package com.sycoldstorage.wms.service;

import com.sycoldstorage.wms.entity.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminService extends UserDetailsService {
    Admin createAdmin(Admin admin);

    Admin getAdminById(String id);
}
