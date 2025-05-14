package org.example.service;

import org.example.model.Item;
import org.example.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public Boolean deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return true;
        }

        itemRepository.deleteById(id);
        return true;
    }
}
