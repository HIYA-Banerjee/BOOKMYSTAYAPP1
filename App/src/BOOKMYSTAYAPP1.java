import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
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
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Standard", 1);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 0); // deliberately 0 to trigger validation
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailableRooms(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        inventory.put(roomType, count - 1);
    }
}

// Validator Class
class BookingValidator {

    public static void validate(Reservation reservation, InventoryService inventory)
            throws InvalidBookingException {

        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        if (inventory.getAvailableRooms(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "No availability for room type: " + reservation.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    public void processBooking(Reservation reservation, InventoryService inventory) {

        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate room (only if valid)
            inventory.decrementRoom(reservation.getRoomType());

            System.out.println("Booking SUCCESS -> Guest: "
                    + reservation.getGuestName()
                    + ", Room Type: " + reservation.getRoomType());

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking FAILED -> " + e.getMessage());
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService();

        // Test cases (valid + invalid)

        Reservation r1 = new Reservation("Hima", "Deluxe");     // valid
        Reservation r2 = new Reservation("", "Standard");       // invalid name
        Reservation r3 = new Reservation("Arun", "Luxury");     // invalid type
        Reservation r4 = new Reservation("Priya", "Suite");     // no availability

        bookingService.processBooking(r1, inventory);
        bookingService.processBooking(r2, inventory);
        bookingService.processBooking(r3, inventory);
        bookingService.processBooking(r4, inventory);
    }
}