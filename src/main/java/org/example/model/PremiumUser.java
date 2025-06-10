package org.example.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "premium_user")
public class PremiumUser implements Serializable {
    /*
    P-ID(PK): Unique identifier for each premium user.
    U-ID(FK): Foreign key referencing User table.
    LastPayment: Date of last successful payment.
    NextPayment: Date of next payment.
    Status: Status of premium account (Active or Inactive).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long premiumId;

    @OneToOne
    private User userId;

    private LocalDate lastPayment;

    private LocalDate nextPayment;

    private Status status;

    enum Status {
        ACTIVE,
        INACTIVE
    }
}
