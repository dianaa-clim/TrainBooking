package dto;

import java.util.ArrayList;
import java.util.List;

public class BookingRequest {
    private String customerName;
    private String customerEmail;
    private JourneyOption journeyOption;
    private List<PassengerRequest> passengers = new ArrayList<>();

    public BookingRequest() {
    }

    public BookingRequest(String customerName, String customerEmail,
                          JourneyOption journeyOption, List<PassengerRequest> passengers) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.journeyOption = journeyOption;
        this.passengers = passengers;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public JourneyOption getJourneyOption() {
        return journeyOption;
    }

    public void setJourneyOption(JourneyOption journeyOption) {
        this.journeyOption = journeyOption;
    }

    public List<PassengerRequest> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerRequest> passengers) {
        this.passengers = passengers;
    }
}