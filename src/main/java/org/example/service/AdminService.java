package org.example.service;

import org.example.model.Admin;
import org.example.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin getAdmin(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    public Boolean deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin == null) {
            return true;
        }

        adminRepository.deleteById(id);
        return true;
    }
}
