package org.example.service;

import org.example.model.Auction;
import org.example.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    public Auction saveAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    public Auction getAuction(Long id) {
        return auctionRepository.findById(id).orElse(null);
    }

    public Boolean deleteAuction(Long id) {
        Auction auction = auctionRepository.findById(id).orElse(null);
        if (auction == null) {
            return true;
        }

        auctionRepository.deleteById(id);
        return true;
    }
}
