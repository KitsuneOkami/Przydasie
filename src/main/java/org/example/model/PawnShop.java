package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pawn_shop")
public class PawnShop extends User
{
	private String businessName;
	private String taxId;
	private String payoutDetails;
}
