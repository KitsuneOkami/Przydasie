package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.AdminDao;
import org.example.model.Admin;

import java.util.List;

@Stateless
public class AdminService
{
	@Inject
	private AdminDao adminDao;

	public List<Admin> findAllAdmins()
	{
		return adminDao.findAll();
	}

	public void saveAdmin(Admin admin)
	{
		adminDao.save(admin);
	}

	public Admin getAdmin(Long id)
	{
		return adminDao.find(id);
	}

	public boolean deleteAdmin(Long id)
	{
		Admin admin = getAdmin(id);
		if(admin==null)
			return true;

		adminDao.delete(admin);
		return true;
	}
}
