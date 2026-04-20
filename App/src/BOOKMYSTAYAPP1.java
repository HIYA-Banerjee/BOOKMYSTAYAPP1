import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType + " | " + roomId;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Standard", 1);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking History (Active + Cancelled tracking)
class BookingHistory {
    private Map<String, Reservation> activeBookings = new HashMap<>();
    private Set<String> cancelledBookings = new HashSet<>();

    public void addReservation(Reservation r) {
        activeBookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return activeBookings.get(id);
    }

    public void markCancelled(String id) {
        cancelledBookings.add(id);
        activeBookings.remove(id);
    }

    public boolean isCancelled(String id) {
        return cancelledBookings.contains(id);
    }
}

// Cancellation Service
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              InventoryService inventory) {

        System.out.println("\nAttempting cancellation for: " + reservationId);

        // Validation
        if (history.isCancelled(reservationId)) {
            System.out.println("Cancellation FAILED -> Already cancelled.");
            return;
        }

        Reservation r = history.getReservation(reservationId);

        if (r == null) {
            System.out.println("Cancellation FAILED -> Reservation does not exist.");
            return;
        }

        // Step 1: Push to rollback stack (LIFO tracking)
        rollbackStack.push(r.getRoomId());

        // Step 2: Restore inventory
        inventory.incrementRoom(r.getRoomType());

        // Step 3: Update history
        history.markCancelled(reservationId);

        System.out.println("Cancellation SUCCESS -> Room released: " + r.getRoomId());
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        // Initialize services
        InventoryService inventory = new InventoryService();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("RES101", "Hima", "Deluxe", "DE1234"));
        history.addReservation(new Reservation("RES102", "Arun", "Suite", "SU5678"));

        // Perform cancellations
        cancellationService.cancelBooking("RES101", history, inventory); // valid
        cancellationService.cancelBooking("RES101", history, inventory); // already cancelled
        cancellationService.cancelBooking("RES999", history, inventory); // invalid

        // Display results
        cancellationService.displayRollbackStack();
        inventory.displayInventory();
    }
}