package org.example.filter;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.web.UserSessionBean;

import java.io.IOException;

@WebFilter(urlPatterns = {"/auction_new.xhtml", "/auction_details.xhtml", "/user/*"})
public class AuthFilter implements Filter
{
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

		if(loggedIn)
			chain.doFilter(req, res);
		else
			response.sendRedirect(request.getContextPath()+"/login.xhtml");
	}
}
