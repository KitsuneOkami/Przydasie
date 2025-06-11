package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Pabilo8
 * @since 11.06.2025
 */
@Named
@RequestScoped
@Setter
@Getter
public class CredentialsBean
{
	private String username;
	private String password;

	@Inject
	private SecurityContext securityContext;

	@Inject
	private HttpServletRequest request;

	public String login()
	{
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getResponse();

		UsernamePasswordCredential credential = new UsernamePasswordCredential(username, password);
		AuthenticationParameters authParams = AuthenticationParameters.withParams().credential(credential);

		AuthenticationStatus result = securityContext.authenticate(request, response, authParams);

		return switch(result)
		{
			case SUCCESS ->
			{
				FacesContext.getCurrentInstance().getExternalContext().getFlash().put("username", username);
				yield "/auctions.xhtml?faces-redirect=true";
			}
			case SEND_CONTINUE ->
			{
				FacesContext.getCurrentInstance().responseComplete();
				yield null;
			}
			default ->
			{
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Invalid username or password"));
				yield null;
			}
		};
	}

	public String logout()
	{
		request.getSession().invalidate();
		return "/login.xhtml?faces-redirect=true";
	}
}
