package org.example.controller;

import org.example.model.Auction;
import org.example.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping("/auction")
    public ResponseEntity<Auction> saveAuction(@RequestBody Auction auction) {
        try {
            Auction savedAuction = auctionService.saveAuction(auction);
            return ResponseEntity.ok(savedAuction);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/auction/{id}")
    public ResponseEntity<Auction> updateAuction(@PathVariable("id") Long id, @RequestBody Auction auction) {
        try {
            Auction existingAuction = auctionService.getAuction(id);
            if (existingAuction == null) {
                return ResponseEntity.notFound().build();
            }

            // update auction...
            // existingAuction.setTitle(auction.getTitle());

            return ResponseEntity.ok(auctionService.saveAuction(existingAuction));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/auction/{id}")
    public ResponseEntity<Boolean> deleteAuction(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(auctionService.deleteAuction(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
