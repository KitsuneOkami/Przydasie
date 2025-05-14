package org.example.controller;

import org.example.model.Admin;
import org.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/admin")
    public ResponseEntity<Admin> saveAdmin(@RequestBody Admin admin) {
        try {
            Admin savedAdmin = adminService.saveAdmin(admin);
            return ResponseEntity.ok(savedAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable("id") Long id, @RequestBody Admin admin) {
        try {
            Admin existingAdmin = adminService.getAdmin(id);
            if (existingAdmin == null) {
                return ResponseEntity.notFound().build();
            }

            //update admin...
            //existingAdmin.setName(admin.getName());

            return ResponseEntity.ok(adminService.saveAdmin(existingAdmin));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Boolean> deleteAdmin(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(adminService.deleteAdmin(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
