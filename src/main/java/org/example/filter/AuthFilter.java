package org.example.filter;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.web.UserSessionBean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(urlPatterns = {"/auction_new.xhtml", "/auction_details.xhtml", "/user/*"})
public class AuthFilter implements Filter
{
	private static final Logger logger = Logger.getLogger(AuthFilter.class.getName());
	@Inject
	UserSessionBean userSessionBean;
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException
	{

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		HttpSession session = request.getSession(false);
		boolean loggedIn = (session!=null)&&userSessionBean.isLoggedIn();

		logger.log(Level.INFO, "Filtering request for {0}. User is logged in: {1}", new Object[]{request.getRequestURI(), loggedIn});

		if(loggedIn) {
			chain.doFilter(req, res);
		}
		else {
			logger.log(Level.WARNING, "User not logged in. Redirecting to login page.");
			response.sendRedirect(request.getContextPath()+"/login.xhtml");
		}
	}
}