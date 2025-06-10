package org.example.dao;

import jakarta.ejb.Stateless;
import org.example.model.PremiumUser;
import org.example.util.AbstractDaoImpl;

@Stateless
public class PremiumUserDaoImpl extends AbstractDaoImpl<PremiumUser, Long> implements PremiumUserDao
{
	@Override
	public PremiumUser find(Long primaryKey)
	{
		return getEntityManager().find(PremiumUser.class, primaryKey);
	}
}