package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Admin;
import org.example.service.AdminService;

import java.util.List;

@Named
@RequestScoped
public class AdminBean
{
	@Inject
	private AdminService adminService;

	@Setter
	@Getter
	private Admin admin = new Admin();

	public List<Admin> getAdmins()
	{
		return adminService.findAllAdmins();
	}

	public void save()
	{
		adminService.saveAdmin(admin);
		admin = new Admin();
	}

}
