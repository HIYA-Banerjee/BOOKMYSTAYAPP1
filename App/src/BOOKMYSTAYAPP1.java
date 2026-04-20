import java.util.HashMap;
import java.util.Map;

/**
 * Hotel Booking Management System - Use Case 4
 * Demonstrates read-only search functionality using inventory.
 *
 * @author HIYA
 * @version 4.0
 */

// ----------- Room Domain Model -----------

// Abstract Room class
abstract class Room {
    protected String type;
    protected int beds;
    protected double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public abstract void displayDetails();
}

// Concrete Rooms
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000);
    }

    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2000);
    }

    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }

    public void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
    }
}

// ----------- Inventory (State Holder) -----------

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0); // unavailable
        inventory.put("Suite Room", 2);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return inventory;
    }
}

// ----------- Search Service -----------

class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Read-only search method
    public void searchAvailableRooms() {

        System.out.println("\n--- Available Rooms ---");

        for (Map.Entry<String, Integer> entry : inventory.getAllAvailability().entrySet()) {

            String roomType = entry.getKey();
            int count = entry.getValue();

            // Filter unavailable rooms
            if (count > 0) {

                Room room = createRoom(roomType);
                room.displayDetails();
                System.out.println("Available: " + count + "\n");
            }
        }
    }

    // Factory method to create room objects
    private Room createRoom(String type) {
        switch (type) {
            case "Single Room":
                return new SingleRoom();
            case "Double Room":
                return new DoubleRoom();
            case "Suite Room":
                return new SuiteRoom();
            default:
                return null;
        }
    }
}

// ----------- Main Class -----------

public class RoomSearch {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App v4.0 =====");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Perform search (read-only)
        searchService.searchAvailableRooms();

        System.out.println("\nSearch completed. Inventory unchanged.");
    }
}