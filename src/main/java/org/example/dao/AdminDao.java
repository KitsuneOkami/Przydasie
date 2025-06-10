package org.example.dao;

import org.example.model.Admin;
import org.example.util.AbstractDao;

import java.util.List;

public interface AdminDao extends AbstractDao<Admin, Long>
{
	List<Admin> findAll();
}
