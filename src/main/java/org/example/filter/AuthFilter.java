package org.example.filter;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.User;
import org.example.model.User.Role;
import org.example.service.BansService;
import org.example.util.JSFUtil;
import org.example.web.UserSessionBean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(urlPatterns = {"/auction_new.xhtml", "/auction_details.xhtml", "/bans.xhtml", "/pawnshop_list.xhtml", "/pawnshop_item_create.xhtml"})
public class AuthFilter implements Filter
{
	private static final Logger logger = Logger.getLogger(AuthFilter.class.getName());

	@Inject
	private UserSessionBean userSessionBean;

	@Inject
	private BansService bansService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		HttpSession session = request.getSession(false);
		boolean loggedIn = (session!=null)&&userSessionBean.isLoggedIn();

		logger.log(Level.INFO, "Filtering request for {0}. User is logged in: {1}", new Object[]{request.getRequestURI(), loggedIn});

		if(!loggedIn)
		{
			logger.log(Level.WARNING, "User not logged in. Redirecting to login page.");
			response.sendRedirect(request.getContextPath()+"/login.xhtml");
			return;
		}

		User user = userSessionBean.getUser();

		// Check if the user is banned
		if(bansService.isUserBanned(user))
		{
			logger.log(Level.WARNING, "User {0} is banned. Redirecting to banned page.", user.getName());
			response.sendRedirect(request.getContextPath()+"/banned.xhtml");
			return;
		}

		//Restrict access to admin-only pages
		String requestURI = request.getRequestURI();
		if(requestURI.endsWith("/bans.xhtml")&&user.getRole()!=User.Role.ADMIN)
		{
			logger.log(Level.WARNING, "User {0} does not have ADMIN role. Redirecting to unauthorized page.", user.getName());
			response.sendRedirect(request.getContextPath()+"/auctions.xhtml");
			JSFUtil.addErrorMessage("Dostęp do strony jest dozwolony tylko dla Administratorów.");
			return;
		}
		else if(requestURI.contains("pawnshop")&&user.getRole()!=Role.PAWN_SHOP)
		{
			logger.log(Level.WARNING, "User {0} does not have a PAWN_SHOP role. Redirecting to unauthorized page.", user.getName());
			response.sendRedirect(request.getContextPath()+"/auctions.xhtml");
			JSFUtil.addErrorMessage("Dostęp do strony jest dozwolony tylko dla Właścicieli Lombardów.");
			return;
		}

		chain.doFilter(req, res);
	}
}