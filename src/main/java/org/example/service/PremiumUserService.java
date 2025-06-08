package org.example.service;

import org.example.model.PremiumUser;
import org.example.repository.PremiumUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PremiumUserService {

    @Autowired
    private PremiumUserRepository premiumUserRepository;

    public PremiumUser savePremiumUser(PremiumUser premiumUser) {
        return premiumUserRepository.save(premiumUser);
    }

    public PremiumUser getPremiumUser(Long id) {
        return premiumUserRepository.findById(id).orElse(null);
    }

    public Boolean deletePremiumUser(Long id) {
        PremiumUser premiumUser = premiumUserRepository.findById(id).orElse(null);
        if (premiumUser == null) {
            return true;
        }

        premiumUserRepository.deleteById(id);
        return true;
    }
}
