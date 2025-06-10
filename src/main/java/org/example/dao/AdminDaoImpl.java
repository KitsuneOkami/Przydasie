package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.Admin;
import org.example.util.AbstractDaoImpl;

import java.util.List;

@Stateless
public class AdminDaoImpl extends AbstractDaoImpl<Admin, Long> implements AdminDao
{
	@Override
	public List<Admin> findAll()
	{
		return getEntityManager()
				.createQuery("SELECT a FROM Admin a", Admin.class)
				.getResultList();
	}

	@Override
	public Admin find(Long primaryKey)
	{
		return getEntityManager().find(Admin.class, primaryKey);
	}
}
