package ro.axonsoft.eval.minibank.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.axonsoft.eval.minibank.model.AccountPageResponse;
import ro.axonsoft.eval.minibank.model.AccountResponse;
import ro.axonsoft.eval.minibank.model.CreateAccountRequest;
import ro.axonsoft.eval.minibank.model.TransactionPageResponse;
import ro.axonsoft.eval.minibank.service.AccountService;
import ro.axonsoft.eval.minibank.service.TransactionService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(final AccountService accountService,
                             final TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@RequestBody @Valid final CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{accountId}")
    public AccountResponse getAccountById(@PathVariable Long accountId) {
        return accountService.getAccountById(accountId);
    }

    @GetMapping
    public AccountPageResponse getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return accountService.getAllAccounts(page, size);
    }

    @GetMapping("/{accountId}/transactions")
    public TransactionPageResponse getTransactionsForAccount(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return transactionService.getTransactionsForAccount(accountId, page, size);
    }
}