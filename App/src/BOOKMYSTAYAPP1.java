import java.util.HashMap;
import java.util.Map;

/**
 * Hotel Booking Management System - Use Case 3
 * Demonstrates centralized inventory using HashMap.
 *
 * @author HIYA
 * @version 3.1
 */

// Inventory class (Single Source of Truth)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor - initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a specific room
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (add/remove rooms)
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, inventory.get(roomType) + count);
        } else {
            System.out.println("Room type not found!");
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main class
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App v3.1 =====");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Check availability
        System.out.println("\nAvailable Single Rooms: " +
                inventory.getAvailability("Single Room"));

        // Update inventory (booking 1 single room)
        inventory.updateAvailability("Single Room", -1);

        System.out.println("\nAfter booking 1 Single Room:");

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication finished.");
    }
}