package ro.axonsoft.eval.minibank.model;

import jakarta.persistence.*;
import ro.axonsoft.eval.minibank.model.Currency;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sourceIban;

    @Column(nullable = false)
    private String targetIban;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency targetCurrency;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal exchangeRate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal convertedAmount;

    @Column(unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private Instant createdAt;

    public Transfer() {

    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSourceIban() {
        return sourceIban;
    }
    public void setSourceIban(String sourceIban) {
        this.sourceIban = sourceIban;
    }
    public String getTargetIban() {
        return targetIban;
    }
    public void setTargetIban(String targetIban) {
        this.targetIban = targetIban;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Currency getCurrency() {
        return currency;
    }
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    public Currency getTargetCurrency() {
        return targetCurrency;
    }
    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }
    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
    public String getIdempotencyKey() {
        return idempotencyKey;
    }
    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
