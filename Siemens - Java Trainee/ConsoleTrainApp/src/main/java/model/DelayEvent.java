package model;

import java.time.LocalDateTime;

public class DelayEvent {
    private Long id;
    private Long trainRunId;
    private int delayMinutes;
    private String reason;
    private boolean notifiedCustomers;
    private LocalDateTime createdAt;

    public DelayEvent() {
    }

    public DelayEvent(Long id, Long trainRunId, int delayMinutes, String reason,
                      boolean notifiedCustomers, LocalDateTime createdAt) {
        this.id = id;
        this.trainRunId = trainRunId;
        this.delayMinutes = delayMinutes;
        this.reason = reason;
        this.notifiedCustomers = notifiedCustomers;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrainRunId() {
        return trainRunId;
    }

    public void setTrainRunId(Long trainRunId) {
        this.trainRunId = trainRunId;
    }

    public int getDelayMinutes() {
        return delayMinutes;
    }

    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isNotifiedCustomers() {
        return notifiedCustomers;
    }

    public void setNotifiedCustomers(boolean notifiedCustomers) {
        this.notifiedCustomers = notifiedCustomers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}