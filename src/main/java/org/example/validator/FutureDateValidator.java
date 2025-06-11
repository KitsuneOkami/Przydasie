package org.example.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.ValidatorException;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@FacesValidator("futureDateValidator")
public class FutureDateValidator implements jakarta.faces.validator.Validator<LocalDateTime>
{
	private static final Logger logger = Logger.getLogger(FutureDateValidator.class.getName());
	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, LocalDateTime localDateTime) throws ValidatorException
	{
		String label = (String)uiComponent.getAttributes().get("label");
		logger.log(Level.INFO, "Validating date for component: {0} with value: {1}", new Object[]{label, localDateTime});
		if(localDateTime==null)
		{
			logger.log(Level.WARNING, "Validation failed for {0}: date is null.", label);
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					(label!=null?label+": ": "")+"Data nie może być pusta", null));
		}

		if(localDateTime.isBefore(LocalDateTime.now()))
		{
			logger.log(Level.WARNING, "Validation failed for {0}: date is in the past.", label);
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					(label!=null?label+": ": "")+"Podana data jest przeszła", null));
		}
		logger.log(Level.INFO, "Date validation successful for component: {0}", label);
	}
}
