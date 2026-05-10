package ro.axonsoft.eval.minibank.model;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionResponse {
    private Long id;
    private Instant timestamp;
    private TransactionType type;
    private BigDecimal amount;
    private Currency currency;
    private BigDecimal balanceAfter;
    private String counterpartyIban;
    private Long transferId;

    public TransactionResponse(Long id, Instant timestamp, TransactionType type,
                               BigDecimal amount, Currency currency,
                               BigDecimal balanceAfter, String counterpartyIban,
                               Long transferId) {
        this.id = id;
        this.timestamp = timestamp;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.balanceAfter = balanceAfter;
        this.counterpartyIban = counterpartyIban;
        this.transferId = transferId;
    }

    public Long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public String getCounterpartyIban() {
        return counterpartyIban;
    }

    public Long getTransferId() {
        return transferId;
    }
}