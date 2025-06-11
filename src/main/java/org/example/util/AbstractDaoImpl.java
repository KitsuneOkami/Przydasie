package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;

/**
 * @author Pabilo8
 * @since 10.06.2025
 */
public abstract class AbstractDaoImpl<T, K> implements AbstractDao<T, K>
{
	@PersistenceContext(unitName = "auctionPU")
	@Getter
	private EntityManager entityManager;

	@Override
	public void save(T admin)
	{
		entityManager.persist(admin);
	}

	@Override
	public abstract T find(K primaryKey);

	@Override
	public void update(T entity)
	{
		entityManager.merge(entity);
	}

	@Override
	public void delete(T entity)
	{
		if(entityManager.contains(entity))
			entityManager.remove(entity);
		else
		{
			T managedEntity = entityManager.merge(entity);
			entityManager.remove(managedEntity);
		}
	}
}
