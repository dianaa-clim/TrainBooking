package ro.axonsoft.eval.minibank.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;


public class TransferResponse {
    private Long id;
    private String sourceIban;
    private String targetIban;
    private BigDecimal amount;
    private Currency currency;
    private Currency targetCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;
    private String idempotencyKey;
    private Instant createdAt;

    public TransferResponse(Long id, String sourceIban, String targetIban, BigDecimal amount, Currency currency,
                            Currency targetCurrency, BigDecimal exchangeRate, BigDecimal convertedAmount,
                            String idempotencyKey, Instant createdAt) {
        this.id = id;
        this.sourceIban = sourceIban;
        this.targetIban = targetIban;
        this.amount = amount;
        this.currency = currency;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
        this.convertedAmount = convertedAmount;
        this.idempotencyKey = idempotencyKey;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }
    public String getSourceIban() {
        return sourceIban;
    }
    public String getTargetIban() {
        return targetIban;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public Currency getCurrency() {
        return currency;
    }
    public Currency getTargetCurrency() {
        return targetCurrency;
    }
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }
    public String getIdempotencyKey() {
        return idempotencyKey;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }

}
