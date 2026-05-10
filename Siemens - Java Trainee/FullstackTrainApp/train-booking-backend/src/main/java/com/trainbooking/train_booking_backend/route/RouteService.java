package com.trainbooking.train_booking_backend.route;

import com.trainbooking.train_booking_backend.exception.BadRequestException;
import com.trainbooking.train_booking_backend.exception.ResourceNotFoundException;
import com.trainbooking.train_booking_backend.route.dto.RouteRequest;
import com.trainbooking.train_booking_backend.route.dto.RouteResponse;
import com.trainbooking.train_booking_backend.route.dto.RouteStopRequest;
import com.trainbooking.train_booking_backend.route.mapper.RouteMapper;
import com.trainbooking.train_booking_backend.station.Station;
import com.trainbooking.train_booking_backend.station.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final StationRepository stationRepository;
    private final RouteMapper routeMapper;

    public RouteService(
            RouteRepository routeRepository,
            RouteStopRepository routeStopRepository,
            StationRepository stationRepository,
            RouteMapper routeMapper
    ) {
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
        this.stationRepository = stationRepository;
        this.routeMapper = routeMapper;
    }

    @Transactional(readOnly = true)
    public List<RouteResponse> getAllRoutes() {
        return routeRepository.findAll()
                .stream()
                .map(routeMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RouteResponse> getActiveRoutes() {
        return routeRepository.findByActiveTrue()
                .stream()
                .map(routeMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RouteResponse getRouteById(Long id) {
        Route route = findRouteById(id);
        return routeMapper.toResponse(route);
    }

    @Transactional
    public RouteResponse createRoute(RouteRequest request) {
        String code = normalizeCode(request.getCode());

        if (routeRepository.existsByCode(code)) {
            throw new BadRequestException("Route code already exists.");
        }

        Route route = routeMapper.toEntity(request);
        Route savedRoute = routeRepository.save(route);

        return routeMapper.toResponse(savedRoute);
    }

    @Transactional
    public RouteResponse updateRoute(Long id, RouteRequest request) {
        Route route = findRouteById(id);

        String newCode = normalizeCode(request.getCode());

        routeRepository.findByCode(newCode)
                .ifPresent(existingRoute -> {
                    if (!existingRoute.getId().equals(id)) {
                        throw new BadRequestException("Route code already exists.");
                    }
                });

        routeMapper.updateEntity(route, request);

        Route updatedRoute = routeRepository.save(route);

        return routeMapper.toResponse(updatedRoute);
    }

    @Transactional
    public RouteResponse deactivateRoute(Long id) {
        Route route = findRouteById(id);

        route.setActive(false);

        Route updatedRoute = routeRepository.save(route);

        return routeMapper.toResponse(updatedRoute);
    }

    @Transactional
    public RouteResponse activateRoute(Long id) {
        Route route = findRouteById(id);

        route.setActive(true);

        Route updatedRoute = routeRepository.save(route);

        return routeMapper.toResponse(updatedRoute);
    }

    @Transactional
    public RouteResponse addStopToRoute(Long routeId, RouteStopRequest request) {
        Route route = findRouteById(routeId);
        Station station = findStationById(request.getStationId());

        if (!station.isActive()) {
            throw new BadRequestException("Cannot add an inactive station to a route.");
        }

        if (routeStopRepository.existsByRouteIdAndStationId(routeId, station.getId())) {
            throw new BadRequestException("This station already exists on this route.");
        }

        if (routeStopRepository.existsByRouteIdAndStopOrder(routeId, request.getStopOrder())) {
            throw new BadRequestException("This stop order already exists on this route.");
        }

        RouteStop routeStop = new RouteStop();
        routeStop.setRoute(route);
        routeStop.setStation(station);
        routeStop.setStopOrder(request.getStopOrder());
        routeStop.setArrivalTime(request.getArrivalTime());
        routeStop.setDepartureTime(request.getDepartureTime());
        routeStop.setActive(true);

        routeStopRepository.save(routeStop);
        route.getStops().add(routeStop);

        return routeMapper.toResponse(route);
    }

    @Transactional
    public RouteResponse updateRouteStop(Long routeId, Long stopId, RouteStopRequest request) {
        Route route = findRouteById(routeId);
        RouteStop routeStop = findRouteStopById(stopId);

        if (!routeStop.getRoute().getId().equals(routeId)) {
            throw new BadRequestException("Route stop does not belong to this route.");
        }

        Station station = findStationById(request.getStationId());

        if (!station.isActive()) {
            throw new BadRequestException("Cannot use an inactive station.");
        }

        routeStopRepository.findByRouteIdAndStationId(routeId, station.getId())
                .ifPresent(existingStop -> {
                    if (!existingStop.getId().equals(stopId)) {
                        throw new BadRequestException("This station already exists on this route.");
                    }
                });

        routeStopRepository.findByRouteIdAndStopOrder(routeId, request.getStopOrder())
                .ifPresent(existingStop -> {
                    if (!existingStop.getId().equals(stopId)) {
                        throw new BadRequestException("This stop order already exists on this route.");
                    }
                });

        routeStop.setStation(station);
        routeStop.setStopOrder(request.getStopOrder());
        routeStop.setArrivalTime(request.getArrivalTime());
        routeStop.setDepartureTime(request.getDepartureTime());

        routeStopRepository.save(routeStop);

        return routeMapper.toResponse(route);
    }

    @Transactional
    public RouteResponse removeStopFromRoute(Long routeId, Long stopId) {
        Route route = findRouteById(routeId);
        RouteStop routeStop = findRouteStopById(stopId);

        if (!routeStop.getRoute().getId().equals(routeId)) {
            throw new BadRequestException("Route stop does not belong to this route.");
        }

        route.getStops().removeIf(stop -> stop.getId().equals(stopId));
        routeStopRepository.delete(routeStop);

        return routeMapper.toResponse(route);
    }

    public Route findRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));
    }

    private RouteStop findRouteStopById(Long id) {
        return routeStopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route stop not found with id: " + id));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id: " + id));
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }
}