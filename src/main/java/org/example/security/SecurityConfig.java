package org.example.security;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;

@ApplicationScoped
@FormAuthenticationMechanismDefinition(
		loginToContinue = @LoginToContinue(
				loginPage = "/login.xhtml",
				errorPage = "/login.xhtml?error=true"
		)
)
@DeclareRoles({"USER", "ADMIN", "PAWN_SHOP"})
public class SecurityConfig
{
}
