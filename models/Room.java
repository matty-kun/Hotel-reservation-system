package models;

import java.io.Serializable;

public class Room implements Serializable {
    private int roomId;
    private String roomType;
    private double price;
    private boolean availability;
    private String description; // New field to store the room description

    public Room(int roomId, String roomType, double price, boolean availability, String description) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.price = price;
        this.availability = availability;
        this.description = description;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getDescription() { // Getter for the description
        return description;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomType='" + roomType + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                ", description='" + description + '\'' +
                '}';
    }
}