package org.example.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "seller")
public class Seller implements Serializable{
    /*
    U-ID(FK): Foreign key referencing User table.
    S-ID(PK): Unique identifier for each seller.
    Rating: Seller's rating based on past transactions.
    Sold Items: Items sold by the seller.
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @OneToOne
    private User userId;

    private float rating;

    private int soldItems;
}
