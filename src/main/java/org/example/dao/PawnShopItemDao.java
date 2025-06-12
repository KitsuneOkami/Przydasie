package org.example.dao;

import org.example.model.PawnShopItem;
import org.example.util.AbstractDao;

import java.util.List;

public interface PawnShopItemDao extends AbstractDao<PawnShopItem, Long>
{
    List<PawnShopItem> findAll();
    List<PawnShopItem> findByOwnerId(Long ownerId);
}