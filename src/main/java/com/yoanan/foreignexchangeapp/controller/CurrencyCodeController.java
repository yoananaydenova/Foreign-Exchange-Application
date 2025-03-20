package com.yoanan.foreignexchangeapp.controller;

import com.yoanan.foreignexchangeapp.service.CurrencyCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyCodeController {

    private final CurrencyCodeService syncService;

    public CurrencyCodeController(CurrencyCodeService syncService) {
        this.syncService = syncService;
    }


    @PostMapping("/sync")
    public ResponseEntity<String> triggerSync() {
        syncService.importCurrencyCodes();
        return ResponseEntity.ok("Currency codes imported");
    }
}
