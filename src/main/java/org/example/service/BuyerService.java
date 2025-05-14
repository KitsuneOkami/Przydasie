package org.example.service;

import org.example.model.Buyer;
import org.example.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerService {

    @Autowired
    private BuyerRepository buyerRepository;

    public Buyer saveBuyer(Buyer buyer) {
        return buyerRepository.save(buyer);
    }

    public Buyer getBuyer(Long id) {
        return buyerRepository.findById(id).orElse(null);
    }

    public Boolean deleteBuyer(Long id) {
        Buyer buyer = buyerRepository.findById(id).orElse(null);
        if (buyer == null) {
            return true;
        }

        buyerRepository.deleteById(id);
        return true;
    }
}
