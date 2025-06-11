package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	/**
	 * Username of the user
	 */
	@Column(nullable = false, unique = true)
	private String name;

	/**
	 * Email address of the user
	 */
	@Column(nullable = false, unique = true)
	private String email;

	/**
	 * Encrypted password of the user
	 */
	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	protected Role role;

	public enum Role
	{
		USER,
		ADMIN,
		PAWN_SHOP
	}
}
