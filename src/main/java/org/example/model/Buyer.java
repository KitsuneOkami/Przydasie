package org.example.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "buyer")
public class Buyer implements Serializable{
    /*
    U-ID(FK): Foreign key referencing User table.
    B-ID(PK): Unique identifier for each buyer.
    BidHistory: History of bids placed by the buyer.
    Phone: Contact number of the buyer.
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buyerId;

    @OneToOne
    private User userId;

    private String bidHistory;

    private String phone;
}
