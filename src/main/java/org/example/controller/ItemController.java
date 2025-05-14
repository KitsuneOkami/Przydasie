package org.example.controller;

import org.example.model.Item;
import org.example.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/item")
    public ResponseEntity<Item> saveItem(@RequestBody Item item) {
        try {
            Item savedItem = itemService.saveItem(item);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable("id") Long id, @RequestBody Item item) {
        try {
            Item existingItem = itemService.getItem(id);
            if (existingItem == null) {
                return ResponseEntity.notFound().build();
            }

            // update item...
            // existingItem.setName(item.getName());

            return ResponseEntity.ok(itemService.saveItem(existingItem));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<Boolean> deleteItem(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(itemService.deleteItem(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
