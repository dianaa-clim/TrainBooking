package dto;

public class PassengerRequest {
    private String fullName;

    public PassengerRequest() {
    }

    public PassengerRequest(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}