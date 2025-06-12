package org.example.dao;

import org.example.model.Ban;
import org.example.util.AbstractDao;

import java.util.List;

public interface BanDao extends AbstractDao<Ban, Long>
{
	List<Ban> findAll();

	List<Ban> findAllEagerUsers();

	boolean isUserBanned(Long userId);
}