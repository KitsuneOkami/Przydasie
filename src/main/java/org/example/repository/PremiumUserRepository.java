package org.example.repository;

import org.example.model.PremiumUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PremiumUserRepository extends JpaRepository<PremiumUser, Long> {

}
