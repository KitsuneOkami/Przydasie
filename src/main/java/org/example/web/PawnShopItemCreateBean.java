package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.PawnShopItem;
import org.example.model.User;
import org.example.service.PawnShopItemService;
import org.example.util.JSFUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class PawnShopItemCreateBean
{
	private static final Logger logger = Logger.getLogger(PawnShopItemCreateBean.class.getName());

	@Inject
	private PawnShopItemService pawnShopItemService;

	@Inject
	private UserSessionBean userSession;

	@Getter
	@Setter
	private String name, description;

	@Getter
	@Setter
	private BigDecimal price;

	public void save()
	{
		logger.log(Level.INFO, "User {0} attempting to create pawn shop item ''{1}''", new Object[]{userSession.getUsername(), name});
		User user = userSession.getUser();
		if(user==null)
		{
			logger.log(Level.SEVERE, "Pawn shop item creation failed: User not logged in.");
			return;
		}

		PawnShopItem item = new PawnShopItem();
		item.setName(name);
		item.setDescription(description);
		item.setPrice(price);
		item.setOwner(user);

		pawnShopItemService.saveItem(item);
		logger.log(Level.INFO, "Pawn shop item ''{0}'' created successfully by user ''{1}''", new Object[]{name, user.getName()});

		try
		{
			JSFUtil.redirect("pawnshop_list.xhtml");
		} catch(IOException e)
		{
			logger.log(Level.SEVERE, "Error redirecting after pawn shop item creation.", e);
		}
	}
}