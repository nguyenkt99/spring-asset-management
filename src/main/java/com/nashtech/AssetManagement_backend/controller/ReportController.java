package com.nashtech.AssetManagement_backend.controller;

import com.nashtech.AssetManagement_backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    ReportService reportService;
    @GetMapping
    public ResponseEntity<?> getAllReport(){
        return reportService.getAll();
    }
}
