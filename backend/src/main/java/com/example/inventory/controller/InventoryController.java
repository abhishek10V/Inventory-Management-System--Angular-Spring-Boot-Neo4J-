package com.example.inventory.controller;

import com.example.inventory.model.*;
import com.example.inventory.service.DeviceService;
import com.example.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private DeviceService deviceService;

    // Get all shelves
    @GetMapping("/shelf")
    public ResponseEntity<List<ShelfVO>> getShelves() {
        List<ShelfVO> shelves = inventoryService.getShelf();
        return ResponseEntity.ok(shelves);
    }

    // Get all shelf positions
    @GetMapping("/shelfPosition")
    public ResponseEntity<List<ShelfPositionVO>> getShelfPositions() {
        List<ShelfPositionVO> shelfPositions = inventoryService.getShelfPosition();
        return ResponseEntity.ok(shelfPositions);
    }

    // Get shelf by ID
    @GetMapping("/shelf/{id}")
    public ResponseEntity<ShelfVO> getShelfById(@PathVariable("id") Long id) {
        ShelfVO shelf = inventoryService.getShelfById(id);
        return ResponseEntity.ok(shelf);
    }

    // Get shelf position by ID
    @GetMapping("/shelfPosition/{id}")
    public ResponseEntity<ShelfPositionVO> getShelfPositionById(@PathVariable("id") Long id) {
        ShelfPositionVO shelfPosition = inventoryService.getShelfPositionById(id);
        return ResponseEntity.ok(shelfPosition);
    }

    // Save a new shelf
    @PostMapping("/shelf")
    public ResponseEntity<ShelfVO> saveShelf(@RequestBody ShelfVO shelf) {
        ShelfVO savedShelf = inventoryService.saveShelf(shelf);
        return ResponseEntity.ok(savedShelf);
    }

    // Save a new shelf position
    @PostMapping("/shelfPosition")
    public ResponseEntity<ShelfPositionVO> saveShelfPosition(@RequestBody ShelfPositionVO shelfPosition) {
        ShelfPositionVO savedShelfPosition = inventoryService.saveShelfPosition(shelfPosition);
        return ResponseEntity.ok(savedShelfPosition);
    }

    // Add shelf position to a device
    @PostMapping("/relationship/device/shelfPosition")
    public ResponseEntity<?> addShelfPositionToDevice(@RequestBody DeviceToShelfPosition req) {
        try {
            inventoryService.addShelfPositionToDevice(req.getDeviceId(), req.getShelfPositionId());
            return ResponseEntity.ok().body("{\"message\": \"Shelf position assigned to device successfully.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }


    // Add a shelf to a shelf position

    @PostMapping("/relationship/shelfPosition/shelf")
    public ResponseEntity<?> addShelfToShelfPosition(@RequestBody ShelfPositionToShelf request) {
        try {
            inventoryService.addShelfToShelfPosition(request.getShelfPositionId(), request.getShelfId());
            return ResponseEntity.ok("Shelf assigned successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error assigning shelf to shelf position: " + e.getMessage());
        }
    }



}
