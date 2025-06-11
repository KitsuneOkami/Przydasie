package org.example.dao;

import org.example.model.Bid;
import org.example.util.AbstractDao;

import java.util.List;

public interface BidDao extends AbstractDao<Bid, Long>
{
	List<Bid> findAll();


}