package org.example.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.ValidatorException;

import java.time.LocalDateTime;

@FacesValidator("futureDateValidator")
public class FutureDateValidator implements jakarta.faces.validator.Validator<LocalDateTime>
{
	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, LocalDateTime localDateTime) throws ValidatorException
	{
		// Retrieve the label of the component
		String label = (String)uiComponent.getAttributes().get("label");

		//If the date is null, throw a validation exception
		if(localDateTime==null)
		{
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					(label!=null?label+": ": "")+"Data nie może być pusta", null));
		}

		if(localDateTime.isBefore(LocalDateTime.now()))
		{
			//If the date is in the past, throw a validation exception
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					(label!=null?label+": ": "")+"Podana data jest przeszła", null));
		}
	}
}