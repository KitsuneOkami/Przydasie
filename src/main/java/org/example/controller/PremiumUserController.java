package org.example.controller;

import org.example.model.PremiumUser;
import org.example.service.PremiumUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PremiumUserController {

    @Autowired
    private PremiumUserService premiumUserService;

    @PostMapping("/premium_user")
    public ResponseEntity<PremiumUser> savePremiumUser(@RequestBody PremiumUser premiumUser) {
        try {
            PremiumUser savedPremiumUser = premiumUserService.savePremiumUser(premiumUser);
            return ResponseEntity.ok(savedPremiumUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/premium_user/{id}")
    public ResponseEntity<PremiumUser> updatePremiumUser(@PathVariable("id") Long id, @RequestBody PremiumUser premiumUser) {
        try {
            PremiumUser existingPremiumUser = premiumUserService.getPremiumUser(id);
            if (existingPremiumUser == null) {
                return ResponseEntity.notFound().build();
            }

            // update user...
            // existingPremiumUser.setUsername(user.getUsername());

            return ResponseEntity.ok(premiumUserService.savePremiumUser(existingPremiumUser));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/premium_user/{id}")
    public ResponseEntity<Boolean> deletePremiumUser(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(premiumUserService.deletePremiumUser(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
