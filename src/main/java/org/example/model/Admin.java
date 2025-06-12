package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.logging.Logger;

@Data
@Entity
@Table(name = "admin")
public class Admin extends User
{
	private static final Logger logger = Logger.getLogger(Admin.class.getName());
	private String firstName;
	private String lastName;
}