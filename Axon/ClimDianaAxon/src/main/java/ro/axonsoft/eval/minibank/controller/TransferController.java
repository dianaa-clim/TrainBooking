package ro.axonsoft.eval.minibank.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.axonsoft.eval.minibank.model.CreateTransferRequest;
import ro.axonsoft.eval.minibank.model.TransferPageResponse;
import ro.axonsoft.eval.minibank.model.TransferResponse;
import ro.axonsoft.eval.minibank.service.TransferService;

import java.time.Instant;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(
            @Valid @RequestBody CreateTransferRequest request) {
        TransferResponse response = transferService.createTransfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{transferId}")
    public ResponseEntity<TransferResponse> getTransferById(@PathVariable Long transferId) {
        TransferResponse response = transferService.getTransferById(transferId);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<TransferPageResponse> getTransfers(
            @RequestParam(required = false) String iban,
            @RequestParam(required = false) Instant fromDate,
            @RequestParam(required = false) Instant toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        TransferPageResponse response = transferService.getTransfers(iban, fromDate, toDate, page, size);
        return ResponseEntity.ok(response);
    }
}
