package dto;

import java.time.LocalDate;

public class JourneySearchRequest {
    private String departureStationCode;
    private String arrivalStationCode;
    private LocalDate travelDate;

    public JourneySearchRequest() {
    }

    public JourneySearchRequest(String departureStationCode, String arrivalStationCode, LocalDate travelDate) {
        this.departureStationCode = departureStationCode;
        this.arrivalStationCode = arrivalStationCode;
        this.travelDate = travelDate;
    }

    public String getDepartureStationCode() {
        return departureStationCode;
    }

    public void setDepartureStationCode(String departureStationCode) {
        this.departureStationCode = departureStationCode;
    }

    public String getArrivalStationCode() {
        return arrivalStationCode;
    }

    public void setArrivalStationCode(String arrivalStationCode) {
        this.arrivalStationCode = arrivalStationCode;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }
}