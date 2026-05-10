package dto;

public class DelayNotificationRecipient {
    private Long bookingId;
    private String bookingCode;
    private String customerName;
    private String customerEmail;

    public DelayNotificationRecipient() {
    }

    public DelayNotificationRecipient(Long bookingId, String bookingCode, String customerName, String customerEmail) {
        this.bookingId = bookingId;
        this.bookingCode = bookingCode;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}