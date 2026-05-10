package ro.axonsoft.eval.minibank.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ro.axonsoft.eval.minibank.model.Transaction;
import ro.axonsoft.eval.minibank.model.TransactionPageResponse;
import ro.axonsoft.eval.minibank.model.TransactionResponse;
import ro.axonsoft.eval.minibank.repository.AccountRepository;
import ro.axonsoft.eval.minibank.repository.TransactionRepository;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public TransactionPageResponse getTransactionsForAccount(Long accountId, int page, int size) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Transaction> transactionPage =
                transactionRepository.findByAccountIdOrderByTimestampAsc(accountId, pageable);

        List<TransactionResponse> content = transactionPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new TransactionPageResponse(
                content,
                transactionPage.getTotalElements(),
                transactionPage.getTotalPages(),
                transactionPage.getNumber(),
                transactionPage.getSize()
        );
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getTimestamp(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getBalanceAfter(),
                transaction.getCounterpartyIban(),
                transaction.getTransferId()
        );
    }
}