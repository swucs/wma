package com.sycoldstorage.wms.service.impl;


import com.sycoldstorage.wms.entity.Admin;
import com.sycoldstorage.wms.repository.AdminRepository;
import com.sycoldstorage.wms.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final Environment env;

    /**
     * 어드민 생성
     * @param admin
     * @return
     */
    @Override
    public Admin createAdmin(Admin admin) {
        admin.encodePassword(passwordEncoder);
        adminRepository.save(admin);
        return admin;
    }

    /**
     * ID로 어드민 조회
     * @param id
     * @return
     */
    @Override
    public Admin getAdminById(String id) {
        Optional<Admin> adminOptional = adminRepository.findById(id);

        if (adminOptional.isEmpty()) {
            throw new UsernameNotFoundException(id);
        }

        return adminOptional.get();
    }

    /**
     * Spring Security의 UserDetailsService 구현 메소드.
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> adminOptional = adminRepository.findById(username);

        if (!adminOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }

        Admin admin = adminOptional.get();

        return new User(admin.getId(), admin.getPassword()
                , true, true
                , true, true
                , new ArrayList<>());
    }
}
