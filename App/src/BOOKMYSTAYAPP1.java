import java.util.LinkedList;
import java.util.Queue;

    // Reservation class represents a booking request
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

        @Override
        public String toString() {
            return "Guest: " + guestName + ", Room Type: " + roomType;
        }
    }

    // Main class
    public class UseCase5BookingRequestQueue {

        public static void main(String[] args) {

            // Queue to store booking requests (FIFO)
            Queue<Reservation> bookingQueue = new LinkedList<>();

            // Step 1: Accept booking requests
            bookingQueue.add(new Reservation("Hima", "Deluxe"));
            bookingQueue.add(new Reservation("Arun", "Suite"));
            bookingQueue.add(new Reservation("Priya", "Standard"));

            System.out.println("Booking requests received and added to queue.\n");

            // Step 2: Display queue (arrival order preserved)
            System.out.println("Current Booking Queue:");
            for (Reservation r : bookingQueue) {
                System.out.println(r);
            }

            // Step 3: Process requests (FIFO)
            System.out.println("\nProcessing Booking Requests (FIFO Order):");

            while (!bookingQueue.isEmpty()) {
                Reservation request = bookingQueue.poll(); // removes first element
                System.out.println("Processing -> " + request);
            }

            System.out.println("\nAll booking requests processed.");
        }
    }
}
