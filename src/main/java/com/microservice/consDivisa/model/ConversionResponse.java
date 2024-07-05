package com.microservice.consDivisa.model;

import java.math.BigDecimal;

public class ConversionResponse {

    private String fechaConversion;
    private BigDecimal tasaOrigen;
    private BigDecimal tasaDestino;
    private BigDecimal montoOriginal;
    private BigDecimal montoResultado;

    public ConversionResponse() {
        // Constructor vacío requerido por Spring para deserialización JSON
    }

    public ConversionResponse(String fechaConversion, BigDecimal tasaOrigen, BigDecimal tasaDestino, BigDecimal montoOriginal, BigDecimal montoResultado) {
        this.fechaConversion = fechaConversion;
        this.tasaOrigen = tasaOrigen;
        this.tasaDestino = tasaDestino;
        this.montoOriginal = montoOriginal;
        this.montoResultado = montoResultado;
    }

    // Getters y Setters
    public String getfechaConversion() {
        return fechaConversion;
    }

    public void setfechaConversion(String fechaConversion) {
        this.fechaConversion = fechaConversion;
    }

    public BigDecimal gettasaOrigen() {
        return tasaOrigen;
    }

    public void settasaOrigen(BigDecimal tasaOrigen) {
        this.tasaOrigen = tasaOrigen;
    }

    public BigDecimal gettasaDestino() {
        return tasaDestino;
    }

    public void settasaDestino(BigDecimal tasaDestino) {
        this.tasaDestino = tasaDestino;
    }

    public BigDecimal getmontoOriginal() {
        return montoOriginal;
    }

    public void setmontoOriginal(BigDecimal montoOriginal) {
        this.montoOriginal = montoOriginal;
    }

    public BigDecimal getmontoResultado() {
        return montoResultado;
    }

    public void setmontoResultado(BigDecimal montoResultado) {
        this.montoResultado = montoResultado;
    }
}