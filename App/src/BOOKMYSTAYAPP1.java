import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// System State (Inventory + Booking History)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nState saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("\nState loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("\nNo saved data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nError loading state. Starting with safe defaults.");
        }

        // Return safe default state
        return new SystemState(new HashMap<>(), new ArrayList<>());
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();

        // Step 1: Load previous state (Recovery)
        SystemState state = persistence.load();

        // If fresh start, initialize default inventory
        if (state.inventory.isEmpty()) {
            state.inventory.put("Standard", 2);
            state.inventory.put("Deluxe", 1);
        }

        // Step 2: Simulate booking activity
        state.bookingHistory.add(new Reservation("RES101", "Hima", "Deluxe"));
        state.inventory.put("Deluxe", state.inventory.get("Deluxe") - 1);

        // Step 3: Display current state
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : state.inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("\n--- Booking History ---");
        for (Reservation r : state.bookingHistory) {
            System.out.println(r);
        }

        // Step 4: Save state before shutdown
        persistence.save(state);

        System.out.println("\nSystem shutdown complete.");
    }
}