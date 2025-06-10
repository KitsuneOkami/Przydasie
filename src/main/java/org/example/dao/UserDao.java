package org.example.dao;

import org.example.model.User;
import org.example.util.AbstractDao;

import java.util.List;

public interface UserDao extends AbstractDao<User, Long>
{
	List<User> findAll();
}