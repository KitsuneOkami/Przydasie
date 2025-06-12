package org.example.web;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Ban;
import org.example.model.User;
import org.example.model.User.Role;
import org.example.service.BansService;
import org.example.service.UserService;
import org.example.util.JSFUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class BanBean
{
	private static final Logger logger = Logger.getLogger(BanBean.class.getName());

	@Inject
	private BansService bansService;
	@Inject
	private UserService userService;
	@Inject
	private UserSessionBean userSession;

	//Shared fields
	@Getter
	@Setter
	private String banUsername;

	//Ban fields
	@Getter
	@Setter
	private String reason;
	@Getter
	@Setter
	private LocalDateTime endDate;
	@Getter
	@Setter
	private Role role = Role.USER;

	//Admin fields
	@Getter
	@Setter
	private String adminUsername;
	@Getter
	@Setter
	private String adminEmail;
	@Getter
	@Setter
	private String adminPassword;
	@Getter
	@Setter
	private String adminFirstName;
	@Getter
	@Setter
	private String adminLastName;

	//Pawn shop fields
	@Getter
	@Setter
	private String shopUsername;
	@Getter
	@Setter
	private String shopEmail;
	@Getter
	@Setter
	private String shopPassword;
	@Getter
	@Setter
	private String businessName;
	@Getter
	@Setter
	private String taxId;
	@Getter
	@Setter
	private String payoutDetails;

	@PostConstruct
	public void init()
	{

	}

	public List<Ban> getBans()
	{
		logger.info("Fetching all bans");
		return bansService.getAllBansEagerUsers();
	}

	public void addPawnShop()
	{
		if(!validateUsername(shopUsername))
			return;

		try
		{
			userService.addPawnShop(shopUsername, shopEmail, shopPassword, businessName, taxId, payoutDetails);
			logger.log(Level.INFO, "Pawn shop added successfully: {0}", shopUsername);
		} catch(Exception e)
		{
			logger.log(Level.SEVERE, "Failed to add pawn shop.", e);
		}
	}

	public void addAdmin()
	{
		if(!validateUsername(adminUsername))
			return;

		try
		{
			userService.addAdmin(adminUsername, adminEmail, adminPassword, adminFirstName, adminLastName);
			logger.log(Level.INFO, "Admin added successfully: {0}", adminUsername);
		} catch(Exception e)
		{
			logger.log(Level.SEVERE, "Failed to add admin.", e);
		}
	}

	public void banUser()
	{
		logger.info("Banning user: "+banUsername+" for reason: "+reason);

		if(!userSession.isLoggedIn()||userSession.getUser().getRole()!=Role.ADMIN)
		{
			logger.warning("You must be an admin to ban users.");
			JSFUtil.addErrorMessage("Musisz być administratorem, aby banować użytkowników.");
			return;
		}

		if(!validateUsername(banUsername))
			return;

		User user = userService.findByName(banUsername);
		if(user==null)
		{
			logger.warning("User with username "+banUsername+" does not exist. Ban operation aborted.");
			JSFUtil.addErrorMessage("Użytkownik o podanej nazwie nie istnieje.");
			return;
		}

		if(reason==null||reason.isEmpty())
		{
			logger.warning("Reason is empty. Ban operation aborted.");
			JSFUtil.addErrorMessage("Powód banowania nie może być pusty.");
			return;
		}

		if(endDate==null)
		{
			logger.warning("End date is empty. Ban operation aborted.");
			JSFUtil.addErrorMessage("Data zakończenia bana nie może być pusta.");
		}

		Ban ban = new Ban();
		ban.setReason(reason);
		ban.setEndDate(endDate);
		ban.setBannedUser(user);
		bansService.save(ban);
		logger.info("User "+banUsername+" has been banned successfully.");
	}

	private boolean validateUsername(String username)
	{
		if(username==null||username.isEmpty())
		{
			logger.warning("Username passed is empty.");
			JSFUtil.addErrorMessage("Nazwa użytkownika nie może być pusta.");
			return false;
		}
		return true;
	}
}