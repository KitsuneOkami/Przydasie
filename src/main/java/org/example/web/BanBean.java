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

	@Getter
	@Setter
	private String reason;

	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private LocalDateTime endDate;

	@Getter
	@Setter
	private Role role = Role.USER;

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

	}

	public void addAdmin()
	{

	}

	public void banUser()
	{
		logger.info("Banning user: "+username+" for reason: "+reason);

		if(!userSession.isLoggedIn()||userSession.getUser().getRole()!=Role.ADMIN)
		{
			logger.warning("You must be an admin to ban users.");
			JSFUtil.addErrorMessage("Musisz być administratorem, aby banować użytkowników.");
			return;
		}

		if(username==null||username.isEmpty())
		{
			logger.warning("Username passed is empty. Ban operation aborted.");
			JSFUtil.addErrorMessage("Nazwa użytkownika nie może być pusta.");
			return;
		}

		User user = userService.findByName(username);
		if(user==null)
		{
			logger.warning("User with username "+username+" does not exist. Ban operation aborted.");
			JSFUtil.addErrorMessage("Użytkownik o podanej nazwie nie istnieje.");
			return;
		}

		if(reason==null||reason.isEmpty())
		{
			logger.warning("Username or reason is empty. Ban operation aborted.");
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
		logger.info("User "+username+" has been banned successfully.");
	}
}