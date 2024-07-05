package com.microservice.consDivisa.controller;

import com.microservice.consDivisa.model.ExchangeRatesApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
public class CurrencyConversionController {

    private static final String EXCHANGE_RATE_API_URL = "http://api.exchangeratesapi.io/v1/latest?access_key=10124780aa73c83cd1e5b667cf8af774";
    //No es buena práctica, pero para la prueba se queman los datos de la moneda para controlar que digite una diferente a las permitidas en la codificación ISO 4217.
    private static final Set<String> MONEDAS_PERMITIDAS = new HashSet<>(Set.of( "AED",    "AFN",    "ALL",    "AMD",    "ANG",    "AOA",    "ARS",    "AUD",    "AWG",    "AZM",
                                                                                "BAM",    "BBD",    "BDT",    "BGN",    "BHD",    "BIF",    "BMD",    "BND",    "BOB",    "BRL",
                                                                                "BSD",    "BTN",    "BWP",    "BYR",    "BZD",    "CAD",    "CDF",    "CHF",    "CLP",    "CNY",
                                                                                "COP",    "CRC",    "RSD",    "CUP",    "CUC",    "CVE",    "CYP",    "CZK",    "DJF",    "DKK",
                                                                                "DOP",    "DZD",    "EEK",    "EGP",    "ERN",    "ETB",    "EUR",    "FJD",    "FKP",    "GBP",
                                                                                "GEL",    "GHS",    "GIP",    "GMD",    "GNF",    "GTQ",    "GYD",    "HKD",    "HNL",    "HRK",    
                                                                                "HTG",    "HUF",    "IDR",    "ILS",    "INR",    "IQD",    "IRR",    "ISK",    "JMD",    "JOD",    
                                                                                "JPY",    "KES",    "KGS",    "KHR",    "KMF",    "KPW",    "KRW",    "KWD",    "KYD",    "KZT",    
                                                                                "LAK",    "LBP",    "LKR",    "LRD",    "LSL",    "LTL",    "LVL",    "LYD",    "MAD",    "MDL",    
                                                                                "MGA",    "MKD",    "MMK",    "MNT",    "MOP",    "MRO",    "MTL",    "MUR",    "MVR",    "MWK",    
                                                                                "MXN",    "MYR",    "MZN",    "NAD",    "NGN",    "NIO",    "NOK",    "NPR",    "NZD",    "OMR",    
                                                                                "PAB",    "PEN",    "PGK",    "PHP",    "PKR",    "PLN",    "PYG",    "QAR",    "RON",    "RUB",    
                                                                                "RWF",    "SAR",    "SBD",    "SCR",    "SDG",    "SEK",    "SGD",    "SHP",    "SKK",    "SLL",    
                                                                                "SOS",    "SRD",    "STD",    "SYP",    "SZL",    "THB",    "TJS",    "TMT",    "TND",    "TOP",    
                                                                                "TRY",    "TTD",    "TWD",    "TZS",    "UAH",    "UGX",    "USD",    "UYU",    "UZS",    "VEF",    
                                                                                "VND",    "VUV",    "WST",    "XAF",    "XBA",    "XBB",    "XBC",    "XBD",    "XCD",    "XDR",    
                                                                                "XFO",    "XFU",    "XOF",    "YER",    "ZAR",    "ZMK",    "ZWL")); 

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/convert")
    public ResponseEntity<?> convertCurrency(
            @RequestParam("monto") BigDecimal monto,
            @RequestParam("monedaOrigen") String monedaOrigen,
            @RequestParam("monedaDestino") String monedaDestino) {

        // Validar parámetros de entrada
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("El monto debe ser mayor que cero.");
        }

        if (!isValidCurrency(monedaOrigen)) {
            return ResponseEntity.badRequest().body("Código de moneda origen inválido.");
        }

        if (!isValidCurrency(monedaDestino)) {
            return ResponseEntity.badRequest().body("Código de moneda destino inválido.");
        }

        // Obtener tasas de cambio de la API.
        String apiUrl = EXCHANGE_RATE_API_URL + "&symbols=" + monedaOrigen + "," + monedaDestino;
        ExchangeRatesApiResponse response = restTemplate.getForObject(apiUrl, ExchangeRatesApiResponse.class);

        if (response == null || response.getRates() == null || response.getRates().isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las tasas de cambio.");
        }

        BigDecimal tasaOrigen = response.getRates().get(monedaOrigen);
        BigDecimal toRate = response.getRates().get(monedaDestino);

        if (tasaOrigen == null || toRate == null) {
            return ResponseEntity.badRequest().body("No se encontró alguna de las monedas especificadas.");
        }

        // Realizar la conversión
        BigDecimal montoDestino = monto.multiply(toRate).divide(tasaOrigen, 2, RoundingMode.HALF_UP);

        // Construir la respuesta
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaConversion = dateFormat.format(new Date());
        BigDecimal montoOriginal = monto.setScale(2, RoundingMode.HALF_UP);

        String responseMessage = buildResponse(fechaConversion, tasaOrigen, montoOriginal, montoDestino);
        return ResponseEntity.ok().body(responseMessage);
    }

    private static boolean isValidCurrency(String currencyCode) {
        return MONEDAS_PERMITIDAS.contains(currencyCode);
    }

    private String buildResponse(String fechaConversion, BigDecimal tasaOrigen, BigDecimal montoOriginal, BigDecimal montoDestino) {
        return String.format(
                "Fecha de conversión: %s\nTasa de conversión: %s\nMonto original: %s \nMonto convertido: %s",
                fechaConversion,
                tasaOrigen,
                montoOriginal,
                montoDestino
        );
    }
}