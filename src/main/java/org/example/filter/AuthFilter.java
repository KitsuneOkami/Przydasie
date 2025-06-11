package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/auctions.xhtml", "/user/*"})
public class AuthFilter implements Filter
{
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException
	{

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		HttpSession session = request.getSession(false);
		boolean loggedIn = (session!=null)&&(session.getAttribute("username")!=null);

		if(loggedIn)
			chain.doFilter(req, res);
		else
		{
			response.sendRedirect(request.getContextPath()+"/login.xhtml");
		}
	}
}
