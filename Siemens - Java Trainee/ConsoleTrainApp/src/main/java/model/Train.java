package model;

public class Train {
    private Long id;
    private String trainNumber;
    private String name;
    private int capacity;
    private boolean active;

    public Train() {
    }

    public Train(Long id, String trainNumber, String name, int capacity, boolean active) {
        this.id = id;
        this.trainNumber = trainNumber;
        this.name = name;
        this.capacity = capacity;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}