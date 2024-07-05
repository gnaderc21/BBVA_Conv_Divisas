package com.microservice.consDivisa.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class ExchangeRatesApiResponse {

    private boolean success;
    private long timestamp;
    private String base;
    private Date date;
    private Map<String, BigDecimal> rates;

    public ExchangeRatesApiResponse() {
        // Constructor vacío requerido por Spring para deserialización JSON
    }

    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}