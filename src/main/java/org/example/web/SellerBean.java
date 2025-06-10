package org.example.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Seller;
import org.example.service.SellerService;

@Named
@RequestScoped
public class SellerBean
{
	@Inject
	private SellerService sellerService;

	@Setter
	@Getter
	private Seller seller = new Seller();

	public void save()
	{
		sellerService.saveSeller(seller);
		seller = new Seller();
	}
}