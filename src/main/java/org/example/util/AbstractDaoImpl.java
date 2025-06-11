package org.example.util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pabilo8
 * @since 10.06.2025
 */
public abstract class AbstractDaoImpl<T, K> implements AbstractDao<T, K>
{
	private static final Logger logger = Logger.getLogger(AbstractDaoImpl.class.getName());

	@PersistenceContext(unitName = "auctionPU")
	@Getter
	private EntityManager entityManager;
	@Override
	public void save(T entity)
	{
		logger.log(Level.INFO, "Saving entity: {0}", entity);
		entityManager.persist(entity);
	}

	@Override
	public abstract T find(K primaryKey);

	@Override
	public void update(T entity)
	{
		logger.log(Level.INFO, "Updating entity: {0}", entity);
		entityManager.merge(entity);
	}

	@Override
	public void delete(T entity)
	{
		logger.log(Level.INFO, "Deleting entity: {0}", entity);
		if(entityManager.contains(entity))
			entityManager.remove(entity);
		else
		{
			T managedEntity = entityManager.merge(entity);
			entityManager.remove(managedEntity);
		}
	}
}
