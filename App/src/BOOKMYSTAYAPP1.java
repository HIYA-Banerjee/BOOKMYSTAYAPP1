import java.util.*;

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

// Thread-safe Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // synchronized method ensures only one thread modifies inventory at a time
    public synchronized boolean allocateRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);

        if (count > 0) {
            inventory.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Shared Booking Queue (Thread-safe access)
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.add(r);
        notifyAll(); // notify waiting threads
    }

    public synchronized Reservation getRequest() {
        while (queue.isEmpty()) {
            try {
                wait(); // wait until request arrives
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }
}

// Booking Processor (Runnable Thread)
class BookingProcessor implements Runnable {

    private BookingQueue queue;
    private InventoryService inventory;

    public BookingProcessor(BookingQueue queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        // Each thread processes 2 requests (simulation)
        for (int i = 0; i < 2; i++) {

            Reservation r = queue.getRequest();

            synchronized (inventory) { // critical section

                boolean success = inventory.allocateRoom(r.getRoomType());

                if (success) {
                    System.out.println(Thread.currentThread().getName()
                            + " -> Booking SUCCESS for "
                            + r.getGuestName()
                            + " (" + r.getRoomType() + ")");
                } else {
                    System.out.println(Thread.currentThread().getName()
                            + " -> Booking FAILED for "
                            + r.getGuestName()
                            + " (" + r.getRoomType() + ")");
                }
            }
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        InventoryService inventory = new InventoryService();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Hima", "Deluxe"));
        queue.addRequest(new Reservation("Arun", "Deluxe"));
        queue.addRequest(new Reservation("Priya", "Standard"));
        queue.addRequest(new Reservation("Kiran", "Standard"));

        // Create multiple threads (guests processed concurrently)
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Final state
        inventory.displayInventory();
    }
}