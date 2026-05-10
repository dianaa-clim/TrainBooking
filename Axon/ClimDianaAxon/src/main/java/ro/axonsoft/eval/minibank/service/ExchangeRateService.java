package ro.axonsoft.eval.minibank.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.axonsoft.eval.minibank.model.Currency;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ExchangeRateService {
    private final Map<Currency, BigDecimal> rates;

    public ExchangeRateService(
            @Value("${exchange.rates.EUR}") double eur,
            @Value("${exchange.rates.USD}") double usd,
            @Value("${exchange.rates.GBP}") double gbp,
            @Value("${exchange.rates.RON}") double ron) {
        this.rates = new EnumMap<>(Currency.class);
        rates.put(Currency.EUR, BigDecimal.valueOf(eur));
        rates.put(Currency.USD, BigDecimal.valueOf(usd));
        rates.put(Currency.GBP, BigDecimal.valueOf(gbp));
        rates.put(Currency.RON, BigDecimal.valueOf(ron));
    }

    public BigDecimal getRate(Currency currency) {
        return rates.get(currency);
    }

    public Map<String, BigDecimal> getExchangeRates() {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        result.put("EUR", rates.get(Currency.EUR));
        result.put("USD", rates.get(Currency.USD));
        result.put("GBP", rates.get(Currency.GBP));
        result.put("RON", rates.get(Currency.RON));
        return result;
    }
}
