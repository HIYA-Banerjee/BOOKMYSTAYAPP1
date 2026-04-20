import java.util.*;

// Service class (Add-On)
class Service {
    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);
    }

    // Get services for a reservation
    public List<Service> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        double total = 0.0;
        for (Service s : getServices(reservationId)) {
            total += s.getCost();
        }
        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<Service> services = getServices(reservationId);

        System.out.println("\nServices for Reservation ID: " + reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (Service s : services) {
            System.out.println("- " + s);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// Main Class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        // Sample reservation IDs (from Use Case 6)
        String res1 = "RES101";
        String res2 = "RES102";

        // Create services
        Service breakfast = new Service("Breakfast", 250);
        Service airportPickup = new Service("Airport Pickup", 800);
        Service spa = new Service("Spa Access", 1200);
        Service wifi = new Service("Premium WiFi", 150);

        // Initialize manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addService(res1, breakfast);
        manager.addService(res1, wifi);
        manager.addService(res1, spa);

        manager.addService(res2, airportPickup);

        // Display results
        manager.displayServices(res1);
        manager.displayServices(res2);
    }
}