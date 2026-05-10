package ro.axonsoft.eval.minibank.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ro.axonsoft.eval.minibank.model.*;
import ro.axonsoft.eval.minibank.repository.AccountRepository;
import ro.axonsoft.eval.minibank.repository.TransactionRepository;
import ro.axonsoft.eval.minibank.repository.TransferRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

@Service
public class TransferService {
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final ExchangeRateService exchangeRateService;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountRepository accountRepository, TransferRepository transferRepository,
                           ExchangeRateService exchangeRateService, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.exchangeRateService = exchangeRateService;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransferResponse createTransfer(CreateTransferRequest request) {
        if(request.getIdempotencyKey() != null) {
            Optional<Transfer> existing = transferRepository.findByIdempotencyKey(request.getIdempotencyKey());
            if(existing.isPresent()) {
                return toResponse(existing.get());
            }
        }
        Account source = accountRepository.findByIban(request.getSourceIban())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source account not found"));
        Account target = accountRepository.findByIban(request.getTargetIban())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target account not found"));

        validateSepa(request.getSourceIban(), request.getTargetIban());

        BigDecimal amount = request.getAmount();
        BigDecimal convertedAmount = null;
        BigDecimal exchangeRate = null;

        boolean isCrossCurrency = source.getCurrency() != target.getCurrency();
        if(isCrossCurrency) {
            exchangeRate = exchangeRateService.getRate(source.getCurrency()).divide(exchangeRateService.getRate(target.getCurrency()),
                    6, RoundingMode.HALF_EVEN);
            convertedAmount = amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN);
        }

        BigDecimal debitAmount = amount;
        BigDecimal creditAmount = isCrossCurrency ? convertedAmount : amount;

        boolean sourceIsBank = source.getIban().equals(AccountService.SYSTEM_BANK_IBAN);
        if (!sourceIsBank && source.getBalance().compareTo(debitAmount) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Insufficient funds");
        }

        if (!sourceIsBank && source.getAccountType() == AccountType.SAVINGS) {
            validateDailyLimit(source, debitAmount);
        }

        if (!sourceIsBank) {
            source.setBalance(source.getBalance().subtract(debitAmount));
            accountRepository.save(source);
        }
        target.setBalance(target.getBalance().add(creditAmount));
        accountRepository.save(target);

        Transfer transfer = new Transfer();
        transfer.setSourceIban(request.getSourceIban());
        transfer.setTargetIban(request.getTargetIban());
        transfer.setAmount(amount);
        transfer.setCurrency(source.getCurrency());
        transfer.setTargetCurrency(target.getCurrency());
        transfer.setExchangeRate(isCrossCurrency ? exchangeRate : null);
        transfer.setConvertedAmount(isCrossCurrency ? convertedAmount : null);
        transfer.setIdempotencyKey(request.getIdempotencyKey());
        transfer.setCreatedAt(Instant.now());

        Transfer saved = transferRepository.save(transfer);
        recordTransactions(source, target, saved, debitAmount, creditAmount);
        return toResponse(saved);
    }

    private TransferResponse toResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getSourceIban(),
                transfer.getTargetIban(),
                transfer.getAmount(),
                transfer.getCurrency(),
                transfer.getTargetCurrency(),
                transfer.getExchangeRate(),
                transfer.getConvertedAmount(),
                transfer.getIdempotencyKey(),
                transfer.getCreatedAt()
        );
    }

    private static final Set<String> SEPA_COUNTRIES = Set.of(
            "AT","BE","BG","HR","CY","CZ","DK","EE","FI","FR","DE","GR","HU",
            "IE","IT","LV","LT","LU","MT","NL","PL","PT","RO","SK","SI","ES",
            "SE","IS","LI","NO","CH","GB","MC","SM","VA","AD","GI"
    );

    private void validateSepa(String sourceIban, String targetIban) {
        String sourceCountry = sourceIban.substring(0, 2);
        String targetCountry = targetIban.substring(0, 2);
        if (!SEPA_COUNTRIES.contains(sourceCountry) ||
                !SEPA_COUNTRIES.contains(targetCountry)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Non-SEPA country");
        }
    }

    private void validateDailyLimit(Account source, BigDecimal amount) {
        Instant startOfDay = LocalDate.now(ZoneOffset.UTC)
                .atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);

        BigDecimal todayTotal = transferRepository
                .sumOutgoingForDay(source.getIban(), startOfDay, endOfDay);
        BigDecimal eurRate = exchangeRateService.getRate(Currency.EUR);
        BigDecimal sourceRate = exchangeRateService.getRate(source.getCurrency());

        BigDecimal amountInEur = amount
                .multiply(sourceRate)
                .divide(eurRate, 2, RoundingMode.HALF_EVEN);

        BigDecimal todayTotalInEur = todayTotal
                .multiply(sourceRate)
                .divide(eurRate, 2, RoundingMode.HALF_EVEN);

        BigDecimal limit = new BigDecimal("5000.00");

        if (todayTotalInEur.add(amountInEur).compareTo(limit) > 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Daily transfer limit exceeded");
        }
    }
    public TransferResponse getTransferById(Long transferId) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found"));

        return toResponse(transfer);
    }

    public TransferPageResponse getTransfers(String iban, Instant fromDate, Instant toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Transfer> transferPage = transferRepository.findTransfersWithFilters(
                iban, fromDate, toDate, pageable
        );

        List<TransferResponse> content = transferPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new TransferPageResponse(
                content,
                transferPage.getTotalElements(),
                transferPage.getTotalPages(),
                transferPage.getNumber(),
                transferPage.getSize()
        );
    }
    private void recordTransactions(Account source, Account target, Transfer saved,
                                    BigDecimal debitAmount, BigDecimal creditAmount) {
        boolean sourceIsBank = source.getIban().equals(AccountService.SYSTEM_BANK_IBAN);
        boolean targetIsBank = target.getIban().equals(AccountService.SYSTEM_BANK_IBAN);

        if (sourceIsBank && !targetIsBank) {
            Transaction deposit = new Transaction();
            deposit.setAccountId(target.getId());
            deposit.setTimestamp(saved.getCreatedAt());
            deposit.setType(TransactionType.DEPOSIT);
            deposit.setAmount(creditAmount);
            deposit.setCurrency(target.getCurrency());
            deposit.setBalanceAfter(target.getBalance());
            deposit.setCounterpartyIban(null);
            deposit.setTransferId(saved.getId());
            transactionRepository.save(deposit);
            return;
        }

        if (!sourceIsBank && targetIsBank) {
            Transaction withdrawal = new Transaction();
            withdrawal.setAccountId(source.getId());
            withdrawal.setTimestamp(saved.getCreatedAt());
            withdrawal.setType(TransactionType.WITHDRAWAL);
            withdrawal.setAmount(debitAmount);
            withdrawal.setCurrency(source.getCurrency());
            withdrawal.setBalanceAfter(source.getBalance());
            withdrawal.setCounterpartyIban(null);
            withdrawal.setTransferId(saved.getId());
            transactionRepository.save(withdrawal);
            return;
        }

        if (!sourceIsBank && !targetIsBank) {
            Transaction out = new Transaction();
            out.setAccountId(source.getId());
            out.setTimestamp(saved.getCreatedAt());
            out.setType(TransactionType.TRANSFER_OUT);
            out.setAmount(debitAmount);
            out.setCurrency(source.getCurrency());
            out.setBalanceAfter(source.getBalance());
            out.setCounterpartyIban(target.getIban());
            out.setTransferId(saved.getId());
            transactionRepository.save(out);

            Transaction in = new Transaction();
            in.setAccountId(target.getId());
            in.setTimestamp(saved.getCreatedAt());
            in.setType(TransactionType.TRANSFER_IN);
            in.setAmount(creditAmount);
            in.setCurrency(target.getCurrency());
            in.setBalanceAfter(target.getBalance());
            in.setCounterpartyIban(source.getIban());
            in.setTransferId(saved.getId());
            transactionRepository.save(in);
        }
    }
}
