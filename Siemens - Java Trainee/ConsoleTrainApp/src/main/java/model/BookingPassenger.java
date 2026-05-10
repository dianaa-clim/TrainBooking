package model;

public class BookingPassenger {
    private Long id;
    private Long bookingId;
    private String fullName;

    public BookingPassenger() {
    }

    public BookingPassenger(Long id, Long bookingId, String fullName) {
        this.id = id;
        this.bookingId = bookingId;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}