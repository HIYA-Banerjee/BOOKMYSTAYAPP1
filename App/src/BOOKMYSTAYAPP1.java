import java.util.*;

// Reservation class (same as previous use case)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> roomInventory;

    public InventoryService() {
        roomInventory = new HashMap<>();
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 2);
        roomInventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return roomInventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementRoom(String roomType) {
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking Service
class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomTypeToIds = new HashMap<>();

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + UUID.randomUUID().toString().substring(0, 4);
    }

    public void processBookings(Queue<Reservation> queue, InventoryService inventory) {

        System.out.println("\nProcessing Booking Requests...\n");

        while (!queue.isEmpty()) {
            Reservation request = queue.poll();
            String roomType = request.getRoomType();

            System.out.println("Processing request for: " + request.getGuestName());

            // Check availability
            if (inventory.isAvailable(roomType)) {

                String roomId;

                // Ensure uniqueness
                do {
                    roomId = generateRoomId(roomType);
                } while (allocatedRoomIds.contains(roomId));

                // Atomic allocation
                allocatedRoomIds.add(roomId);

                roomTypeToIds.putIfAbsent(roomType, new HashSet<>());
                roomTypeToIds.get(roomType).add(roomId);

                inventory.decrementRoom(roomType);

                System.out.println("Booking Confirmed -> Guest: "
                        + request.getGuestName()
                        + ", Room Type: " + roomType
                        + ", Room ID: " + roomId);
            } else {
                System.out.println("Booking Failed (No Availability) -> Guest: "
                        + request.getGuestName()
                        + ", Room Type: " + roomType);
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\nAllocated Rooms:");
        for (Map.Entry<String, Set<String>> entry : roomTypeToIds.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        // Step 1: Create booking queue (from Use Case 5)
        Queue<Reservation> bookingQueue = new LinkedList<>();

        bookingQueue.add(new Reservation("Hima", "Deluxe"));
        bookingQueue.add(new Reservation("Arun", "Suite"));
        bookingQueue.add(new Reservation("Priya", "Standard"));
        bookingQueue.add(new Reservation("Kiran", "Suite")); // will fail (only 1 suite)

        // Step 2: Initialize services
        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService();

        // Step 3: Process bookings
        bookingService.processBookings(bookingQueue, inventory);

        // Step 4: Display results
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}