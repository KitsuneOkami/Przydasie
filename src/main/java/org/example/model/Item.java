package org.example.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "item")
public class Item implements Serializable{
    /*
    I-ID(PK): Unique identifier for each item.
    S-ID(FK): Foreign key referencing seller table.
    A-ID(FK): Foreign key referencing auction table.
    Name: Name of the item.
    Description: Description of the item.
    Status: Status of the item (e.g., available, sold).
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @OneToOne
    private Seller sellerId;

    @OneToOne
    private Auction auctionId;

    private String name;

    private String description;

    private ItemStatus status;

    //status types for auction
    enum ItemStatus {
        AVALIABLE,
        SOLD
    }
}
