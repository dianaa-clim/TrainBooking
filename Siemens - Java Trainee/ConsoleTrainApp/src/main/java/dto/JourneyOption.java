package dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JourneyOption {
    private int optionNumber;
    private List<JourneyLeg> legs = new ArrayList<>();

    public JourneyOption() {
    }

    public JourneyOption(int optionNumber, List<JourneyLeg> legs) {
        this.optionNumber = optionNumber;
        this.legs = legs;
    }

    public boolean isDirect() {
        return legs.size() == 1;
    }

    public LocalDateTime getDepartureTime() {
        if (legs == null || legs.isEmpty()) {
            return null;
        }

        return legs.getFirst().getDepartureTime();
    }

    public LocalDateTime getArrivalTime() {
        if (legs == null || legs.isEmpty()) {
            return null;
        }

        return legs.getLast().getArrivalTime();
    }

    public long getTotalDurationMinutes() {
        if (getDepartureTime() == null || getArrivalTime() == null) {
            return 0;
        }

        return Duration.between(getDepartureTime(), getArrivalTime()).toMinutes();
    }

    public int getOptionNumber() {
        return optionNumber;
    }

    public void setOptionNumber(int optionNumber) {
        this.optionNumber = optionNumber;
    }

    public List<JourneyLeg> getLegs() {
        return legs;
    }

    public void setLegs(List<JourneyLeg> legs) {
        this.legs = legs;
    }
}