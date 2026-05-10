package ro.axonsoft.eval.minibank.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateTransferRequest {
    @NotNull
    private String sourceIban;

    @NotNull
    private String targetIban;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    private String idempotencyKey;

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
    public String getIdempotencyKey() {
        return idempotencyKey;
    }
    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

}
