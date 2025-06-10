package org.example.util;

/**
 * A generic interface for Data Access Objects (DAO) that provides basic CRUD operations.
 *
 * @param <K> the type of the primary key
 * @param <T> the type of the entity
 * @author Pabilo8
 * @since 10.06.2025
 */
public interface AbstractDao<T, K>
{
	void save(T entity);

	T find(K primaryKey);

	void update(T entity);

	void delete(T entity);
}
