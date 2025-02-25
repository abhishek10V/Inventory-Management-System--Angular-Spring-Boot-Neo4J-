package com.example.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node
@AllArgsConstructor
@NoArgsConstructor
public class ShelfPositionVO {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Long deviceId;

    // ✅ Relationship with Shelf (Outgoing)
    @Relationship(type = "HAS", direction = Relationship.Direction.OUTGOING)
    private ShelfVO shelf;

    // ✅ Getter and Setter for Shelf
    public ShelfVO getShelf() {
        return shelf;
    }

    public void setShelf(ShelfVO shelf) {
        this.shelf = shelf;
        if (shelf != null) {
            shelf.setShelfPositionId(this.id); // ✅ Ensure shelfPositionId is updated
        }
    }

    // ✅ Method to set Device ID when assigning a Device
    public void setDevice(Device device) {
        this.deviceId = (device != null) ? device.getId() : null;
    }
}
