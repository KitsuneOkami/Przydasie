package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.logging.Logger;

@Data
@Entity
@Table(name = "pawn_shop")
public class PawnShop extends User
{
	private static final Logger logger = Logger.getLogger(PawnShop.class.getName());
	private String businessName;
	private String taxId;
	private String payoutDetails;
}