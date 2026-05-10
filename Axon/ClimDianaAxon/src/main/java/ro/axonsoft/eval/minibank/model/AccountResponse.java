package ro.axonsoft.eval.minibank.model;

import java.math.BigDecimal;
import java.time.Instant;

public class AccountResponse {
    private Long id;
    private String ownerName;
    private String iban;
    private Currency currency;
    private AccountType accountType;
    private BigDecimal balance;
    private Instant createdAt;

    public AccountResponse(){}

    public AccountResponse(Long id, String ownerName, String iban, Currency currency, AccountType accountType,
                       BigDecimal balance, Instant createdAt){
        this.id = id;
        this.ownerName = ownerName;
        this.iban = iban;
        this.currency = currency;
        this.accountType = accountType;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getIban() {
        return iban;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
