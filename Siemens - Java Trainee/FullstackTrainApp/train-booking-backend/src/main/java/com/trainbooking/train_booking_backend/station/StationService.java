package com.trainbooking.train_booking_backend.station;

import com.trainbooking.train_booking_backend.exception.BadRequestException;
import com.trainbooking.train_booking_backend.exception.ResourceNotFoundException;
import com.trainbooking.train_booking_backend.station.dto.StationRequest;
import com.trainbooking.train_booking_backend.station.dto.StationResponse;
import com.trainbooking.train_booking_backend.station.mapper.StationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;
    private final StationMapper stationMapper;

    public StationService(
            StationRepository stationRepository,
            StationMapper stationMapper
    ) {
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
    }

    public List<StationResponse> getAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(stationMapper::toResponse)
                .toList();
    }

    public List<StationResponse> getActiveStations() {
        return stationRepository.findByActiveTrue()
                .stream()
                .map(stationMapper::toResponse)
                .toList();
    }

    public StationResponse getStationById(Long id) {
        Station station = findStationById(id);

        return stationMapper.toResponse(station);
    }

    @Transactional
    public StationResponse createStation(StationRequest request) {
        String code = normalizeCode(request.getCode());

        if (stationRepository.existsByCode(code)) {
            throw new BadRequestException("Station code already exists.");
        }

        Station station = stationMapper.toEntity(request);
        Station savedStation = stationRepository.save(station);

        return stationMapper.toResponse(savedStation);
    }

    @Transactional
    public StationResponse updateStation(Long id, StationRequest request) {
        Station station = findStationById(id);

        String newCode = normalizeCode(request.getCode());

        stationRepository.findByCode(newCode)
                .ifPresent(existingStation -> {
                    if (!existingStation.getId().equals(id)) {
                        throw new BadRequestException("Station code already exists.");
                    }
                });

        stationMapper.updateEntity(station, request);

        Station updatedStation = stationRepository.save(station);

        return stationMapper.toResponse(updatedStation);
    }

    @Transactional
    public StationResponse deactivateStation(Long id) {
        Station station = findStationById(id);

        station.setActive(false);

        Station updatedStation = stationRepository.save(station);

        return stationMapper.toResponse(updatedStation);
    }

    @Transactional
    public StationResponse activateStation(Long id) {
        Station station = findStationById(id);

        station.setActive(true);

        Station updatedStation = stationRepository.save(station);

        return stationMapper.toResponse(updatedStation);
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id: " + id));
    }

    public Station findActiveStationByCode(String code) {
        return stationRepository.findByCodeAndActiveTrue(normalizeCode(code))
                .orElseThrow(() -> new ResourceNotFoundException("Active station not found with code: " + code));
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }
}