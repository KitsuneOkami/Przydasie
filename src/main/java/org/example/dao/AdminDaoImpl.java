package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Admin;
import org.example.util.AbstractDaoImpl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class AdminDaoImpl extends AbstractDaoImpl<Admin, Long> implements AdminDao
{
	private static final Logger logger = Logger.getLogger(AdminDaoImpl.class.getName());
	@Override
	public List<Admin> findAll()
	{
		logger.log(Level.INFO, "Finding all admins.");
		return getEntityManager()
				.createQuery("SELECT a FROM Admin a", Admin.class)
				.getResultList();
	}

	@Override
	public Admin find(Long primaryKey)
	{
		logger.log(Level.INFO, "Finding Admin with primary key: {0}", primaryKey);
		return getEntityManager().find(Admin.class, primaryKey);
	}
}
