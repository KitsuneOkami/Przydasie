package org.example.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import org.example.model.User;
import org.example.service.UserService;

import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class CustomIdentityStore implements IdentityStore
{
	@Inject
	private UserService userService;

	@Override
	public CredentialValidationResult validate(Credential credential)
	{
		UsernamePasswordCredential upc = (UsernamePasswordCredential)credential;
		Optional<User> userOpt = userService.authenticate(upc.getCaller(), upc.getPasswordAsString());

		if(userOpt.isPresent())
		{
			User user = userOpt.get();
			return new CredentialValidationResult(user.getName(), Set.of(user.getRole().name()));
		}

		return CredentialValidationResult.INVALID_RESULT;
	}
}
