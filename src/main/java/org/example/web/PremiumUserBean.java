package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.PremiumUser;
import org.example.service.PremiumUserService;

@Named
@RequestScoped
public class PremiumUserBean
{
	@Inject
	private PremiumUserService premiumUserService;

	@Setter
	@Getter
	private PremiumUser premiumUser = new PremiumUser();

	public void save()
	{
		premiumUserService.savePremiumUser(premiumUser);
		premiumUser = new PremiumUser();
	}
}