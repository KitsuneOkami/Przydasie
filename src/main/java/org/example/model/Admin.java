package org.example.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "admin")
public class Admin implements Serializable{
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

    @Column(name="username")
    private String username;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;
}
