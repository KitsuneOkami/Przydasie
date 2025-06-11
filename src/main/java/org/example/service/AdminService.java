package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.example.dao.AdminDao;
import org.example.model.Admin;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class AdminService
{
	private static final Logger logger = Logger.getLogger(AdminService.class.getName());
	@Inject
	private AdminDao adminDao;

	public List<Admin> findAllAdmins()
	{
		logger.log(Level.INFO, "Retrieving all admins.");
		return adminDao.findAll();
	}

	public void saveAdmin(Admin admin)
	{
		if (admin == null) {
			logger.log(Level.SEVERE, "Attempt to save a null Admin object.");
			throw new IllegalArgumentException("Admin cannot be null");
		}
		logger.log(Level.INFO, "Saving admin: {0}", admin);
		adminDao.save(admin);
	}

	public Admin getAdmin(Long id)
	{
		logger.log(Level.INFO, "Retrieving admin with ID: {0}", id);
		return adminDao.find(id);
	}

	public boolean deleteAdmin(Long id)
	{
		logger.log(Level.INFO, "Deleting admin with ID: {0}", id);
		Admin admin = getAdmin(id);
		if(admin==null) {
			logger.log(Level.WARNING, "Admin with ID {0} not found for deletion.", id);
			return true;
		}

		adminDao.delete(admin);
		logger.log(Level.INFO, "Admin with ID {0} deleted successfully.", id);
		return true;
	}
}