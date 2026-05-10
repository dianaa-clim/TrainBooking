package com.trainbooking.train_booking_backend.train;

import com.trainbooking.train_booking_backend.exception.BadRequestException;
import com.trainbooking.train_booking_backend.exception.ResourceNotFoundException;
import com.trainbooking.train_booking_backend.train.dto.TrainRequest;
import com.trainbooking.train_booking_backend.train.dto.TrainResponse;
import com.trainbooking.train_booking_backend.train.mapper.TrainMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainService {

    private final TrainRepository trainRepository;
    private final TrainMapper trainMapper;

    public TrainService(
            TrainRepository trainRepository,
            TrainMapper trainMapper
    ) {
        this.trainRepository = trainRepository;
        this.trainMapper = trainMapper;
    }

    public List<TrainResponse> getAllTrains() {
        return trainRepository.findAll()
                .stream()
                .map(trainMapper::toResponse)
                .toList();
    }

    public List<TrainResponse> getActiveTrains() {
        return trainRepository.findByActiveTrue()
                .stream()
                .map(trainMapper::toResponse)
                .toList();
    }

    public TrainResponse getTrainById(Long id) {
        Train train = findTrainById(id);

        return trainMapper.toResponse(train);
    }

    @Transactional
    public TrainResponse createTrain(TrainRequest request) {
        String code = normalizeCode(request.getCode());

        if (trainRepository.existsByCode(code)) {
            throw new BadRequestException("Train code already exists.");
        }

        Train train = trainMapper.toEntity(request);
        Train savedTrain = trainRepository.save(train);

        return trainMapper.toResponse(savedTrain);
    }

    @Transactional
    public TrainResponse updateTrain(Long id, TrainRequest request) {
        Train train = findTrainById(id);

        String newCode = normalizeCode(request.getCode());

        trainRepository.findByCode(newCode)
                .ifPresent(existingTrain -> {
                    if (!existingTrain.getId().equals(id)) {
                        throw new BadRequestException("Train code already exists.");
                    }
                });

        trainMapper.updateEntity(train, request);

        Train updatedTrain = trainRepository.save(train);

        return trainMapper.toResponse(updatedTrain);
    }

    @Transactional
    public TrainResponse deactivateTrain(Long id) {
        Train train = findTrainById(id);

        train.setActive(false);

        Train updatedTrain = trainRepository.save(train);

        return trainMapper.toResponse(updatedTrain);
    }

    @Transactional
    public TrainResponse activateTrain(Long id) {
        Train train = findTrainById(id);

        train.setActive(true);

        Train updatedTrain = trainRepository.save(train);

        return trainMapper.toResponse(updatedTrain);
    }

    public Train findTrainById(Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + id));
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }
}