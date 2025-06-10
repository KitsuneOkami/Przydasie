package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "admin")
public class Admin implements Serializable
{
    /*
    A-ID(PK): Unique identifier for each admin.
    Username: Username of the admin.
    Email: Email of the admin.
    Password: Encrypted password of the admin.
    First Name: First name of the admin.
    Last Name: Last name of the admin.
    */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long adminId;

	private String username;

	private String email;

	private String password;

	private String firstName;

	private String lastName;
}
