package org.example.controller;

import org.example.model.Seller;
import org.example.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/seller")
    public ResponseEntity<Seller> saveSeller(@RequestBody Seller seller) {
        try {
            Seller savedSeller = sellerService.saveSeller(seller);
            return ResponseEntity.ok(savedSeller);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/seller/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable("id") Long id, @RequestBody Seller seller) {
        try {
            Seller existingSeller = sellerService.getSeller(id);
            if (existingSeller == null) {
                return ResponseEntity.notFound().build();
            }

            // update seller...
            // existingSeller.setName(seller.getName());

            return ResponseEntity.ok(sellerService.saveSeller(existingSeller));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/seller/{id}")
    public ResponseEntity<Boolean> deleteSeller(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(sellerService.deleteSeller(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
