package ro.axonsoft.eval.minibank.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ro.axonsoft.eval.minibank.model.Account;
import ro.axonsoft.eval.minibank.model.AccountPageResponse;
import ro.axonsoft.eval.minibank.model.AccountResponse;
import ro.axonsoft.eval.minibank.model.CreateAccountRequest;
import ro.axonsoft.eval.minibank.repository.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    public static final String SYSTEM_BANK_IBAN = "RO49AAAA1B31007593840000";
    public static final String SYSTEM_BANK_OWNER = "MiniBank";
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public AccountResponse createAccount(CreateAccountRequest request) {
        validateIban(request.getIban());
        if (accountRepository.existsByIban(request.getIban())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "IBAN already in use");
        }
        Account account = new Account();
        account.setIban(request.getIban());
        account.setOwnerName(request.getOwnerName());
        account.setAccountType(request.getAccountType());
        account.setBalance(BigDecimal.ZERO.setScale(2));
        account.setCreatedAt(Instant.now());
        account.setCurrency(request.getCurrency());

        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(savedAccount.getId(), savedAccount.getOwnerName(), savedAccount.getIban(),
                savedAccount.getCurrency(), savedAccount.getAccountType(), savedAccount.getBalance(),
                savedAccount.getCreatedAt());
    }

    private void validateIban(String iban) {
        if (iban == null) {
            throw new IllegalArgumentException("IBAN is invalid");
        }

        String cleanedIban = iban.replaceAll("\\s+", "").toUpperCase();

        if (!cleanedIban.matches("^[A-Z]{2}\\d{2}[A-Z0-9]+$")) {
            throw new IllegalArgumentException("IBAN is invalid");
        }

        if (cleanedIban.length() < 15 || cleanedIban.length() > 34) {
            throw new IllegalArgumentException("IBAN is invalid");
        }

        String countryCode = cleanedIban.substring(0, 2);

        if (countryCode.equals("RO") && cleanedIban.length() != 24) {
            throw new IllegalArgumentException("IBAN is invalid");
        }

        String rearrangedIban = cleanedIban.substring(4) + cleanedIban.substring(0, 4);
        StringBuilder numeric = new StringBuilder();

        for (char ch : rearrangedIban.toCharArray()) {
            if (Character.isDigit(ch)) {
                numeric.append(ch);
            } else if (Character.isLetter(ch)) {
                numeric.append(ch - 'A' + 10);
            } else {
                throw new IllegalArgumentException("IBAN is invalid");
            }
        }

        int remainder = 0;
        for (char digit : numeric.toString().toCharArray()) {
            remainder = (remainder * 10 + (digit - '0')) % 97;
        }

        if (remainder != 1) {
            throw new IllegalArgumentException("IBAN is invalid");
        }
    }

    public AccountResponse getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        return new AccountResponse(
                account.getId(),
                account.getOwnerName(),
                account.getIban(),
                account.getCurrency(),
                account.getAccountType(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }

    public AccountPageResponse getAllAccounts(int page, int size) {
        Page<Account> accountPage = accountRepository.findAll(PageRequest.of(page, size));

        List<AccountResponse> content = accountPage.getContent()
                .stream()
                .map(account -> new AccountResponse(
                        account.getId(),
                        account.getOwnerName(),
                        account.getIban(),
                        account.getCurrency(),
                        account.getAccountType(),
                        account.getBalance(),
                        account.getCreatedAt()
                ))
                .toList();

        return new AccountPageResponse(
                content,
                accountPage.getTotalElements(),
                accountPage.getTotalPages(),
                accountPage.getNumber(),
                accountPage.getSize()
        );
    }
}
