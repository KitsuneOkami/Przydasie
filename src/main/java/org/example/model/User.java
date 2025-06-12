package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;
import java.util.logging.Logger;

@Data
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable
{
	private static final Logger logger = Logger.getLogger(User.class.getName());
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Include
	private Long userId;
	/**
	 * Username of the user
	 */
	@Column(nullable = false, unique = true)
	@ToString.Include
	private String name;
	/**
	 * Email address of the user
	 */
	@Column(nullable = false, unique = true)
	@ToString.Include
	private String email;
	/**
	 * Encrypted password of the user
	 */
	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@ToString.Include
	protected Role role;

	@OneToMany(mappedBy = "bidder", fetch = FetchType.EAGER)
	private Set<Bid> bids;

	public enum Role
	{
		USER,
		ADMIN,
		PAWN_SHOP
	}
}