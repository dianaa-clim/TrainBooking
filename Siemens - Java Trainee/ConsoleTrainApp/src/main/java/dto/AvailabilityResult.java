package dto;

public class AvailabilityResult {
    private boolean available;
    private int requestedSeats;
    private int minimumAvailableSeats;
    private String message;

    public AvailabilityResult() {
    }

    public AvailabilityResult(boolean available, int requestedSeats, int minimumAvailableSeats, String message) {
        this.available = available;
        this.requestedSeats = requestedSeats;
        this.minimumAvailableSeats = minimumAvailableSeats;
        this.message = message;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getRequestedSeats() {
        return requestedSeats;
    }

    public void setRequestedSeats(int requestedSeats) {
        this.requestedSeats = requestedSeats;
    }

    public int getMinimumAvailableSeats() {
        return minimumAvailableSeats;
    }

    public void setMinimumAvailableSeats(int minimumAvailableSeats) {
        this.minimumAvailableSeats = minimumAvailableSeats;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}