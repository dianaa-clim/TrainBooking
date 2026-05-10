package ro.axonsoft.eval.minibank.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.axonsoft.eval.minibank.model.Transfer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByIdempotencyKey(String idempotencyKey);
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transfer t " +
            "WHERE t.sourceIban = :iban " +
            "AND t.createdAt >= :startOfDay AND t.createdAt < :endOfDay")
    BigDecimal sumOutgoingForDay(@Param("iban") String iban,
                                 @Param("startOfDay") Instant startOfDay,
                                 @Param("endOfDay") Instant endOfDay);

    @Query("""
    SELECT t FROM Transfer t
    WHERE (:iban IS NULL OR t.sourceIban = :iban OR t.targetIban = :iban)
      AND (:fromDate IS NULL OR t.createdAt >= :fromDate)
      AND (:toDate IS NULL OR t.createdAt <= :toDate)
    ORDER BY t.createdAt DESC
""")
    Page<Transfer> findTransfersWithFilters(@Param("iban") String iban,
                                            @Param("fromDate") Instant fromDate,
                                            @Param("toDate") Instant toDate,
                                            Pageable pageable);
}
