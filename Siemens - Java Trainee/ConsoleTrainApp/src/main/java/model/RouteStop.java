package model;

import java.math.BigDecimal;

public class RouteStop {
    private Long id;
    private Long routeId;
    private Long stationId;
    private int stopOrder;
    private BigDecimal distanceFromStartKm;

    public RouteStop() {
    }

    public RouteStop(Long id, Long routeId, Long stationId, int stopOrder, BigDecimal distanceFromStartKm) {
        this.id = id;
        this.routeId = routeId;
        this.stationId = stationId;
        this.stopOrder = stopOrder;
        this.distanceFromStartKm = distanceFromStartKm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public int getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(int stopOrder) {
        this.stopOrder = stopOrder;
    }

    public BigDecimal getDistanceFromStartKm() {
        return distanceFromStartKm;
    }

    public void setDistanceFromStartKm(BigDecimal distanceFromStartKm) {
        this.distanceFromStartKm = distanceFromStartKm;
    }
}