package ro.axonsoft.eval.minibank.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.axonsoft.eval.minibank.model.Account;
import ro.axonsoft.eval.minibank.model.AccountType;
import ro.axonsoft.eval.minibank.model.Currency;
import ro.axonsoft.eval.minibank.repository.AccountRepository;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public DataInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) {
        if (accountRepository.existsByIban(AccountService.SYSTEM_BANK_IBAN)) {
            return;
        }

        Account bankAccount = new Account();
        bankAccount.setOwnerName(AccountService.SYSTEM_BANK_OWNER);
        bankAccount.setIban(AccountService.SYSTEM_BANK_IBAN);
        bankAccount.setCurrency(Currency.RON);
        bankAccount.setAccountType(AccountType.CHECKING);
        bankAccount.setBalance(new BigDecimal("999999999.99"));
        bankAccount.setCreatedAt(Instant.now());

        accountRepository.save(bankAccount);
    }
}