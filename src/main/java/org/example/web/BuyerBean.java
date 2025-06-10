package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Buyer;
import org.example.service.BuyerService;

@Named
@RequestScoped
public class BuyerBean
{
	@Inject
	private BuyerService buyerService;

	@Setter
	@Getter
	private Buyer buyer = new Buyer();

	public void save()
	{
		buyerService.saveBuyer(buyer);
		buyer = new Buyer();
	}
}