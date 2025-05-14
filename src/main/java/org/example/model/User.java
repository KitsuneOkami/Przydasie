package org.example.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user")
public class User implements Serializable {
    /*
    U-ID(PK): Unique identifier for each user.
    Name: Username of the user.
    Email: Email address of the user.
    Password: Encrypted password of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    private String email;

    private String password;
}
