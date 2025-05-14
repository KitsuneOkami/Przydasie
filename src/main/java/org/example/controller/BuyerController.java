package org.example.controller;

import org.example.model.Buyer;
import org.example.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BuyerController {

    @Autowired
    private BuyerService buyerService;

    @PostMapping("/buyer")
    public ResponseEntity<Buyer> saveBuyer(@RequestBody Buyer buyer) {
        try {
            Buyer savedBuyer = buyerService.saveBuyer(buyer);
            return ResponseEntity.ok(savedBuyer);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/buyer/{id}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable("id") Long id, @RequestBody Buyer buyer) {
        try {
            Buyer existingBuyer = buyerService.getBuyer(id);
            if (existingBuyer == null) {
                return ResponseEntity.notFound().build();
            }

            // update buyer...
            // existingBuyer.setName(buyer.getName());

            return ResponseEntity.ok(buyerService.saveBuyer(existingBuyer));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/buyer/{id}")
    public ResponseEntity<Boolean> deleteBuyer(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(buyerService.deleteBuyer(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
