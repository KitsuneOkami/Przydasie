package org.example.service;

import org.example.model.Seller;
import org.example.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public Seller getSeller(Long id) {
        return sellerRepository.findById(id).orElse(null);
    }

    public Boolean deleteSeller(Long id) {
        Seller seller = sellerRepository.findById(id).orElse(null);
        if (seller == null) {
            return true;
        }

        sellerRepository.deleteById(id);
        return true;
    }
}
