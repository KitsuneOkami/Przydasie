package org.example.dao;

import org.example.model.User;
import org.example.util.AbstractDao;

import java.util.List;
import java.util.Optional;

public interface UserDao extends AbstractDao<User, Long>
{
	Optional<User> findByName(String name);

	List<User> findAll();
}