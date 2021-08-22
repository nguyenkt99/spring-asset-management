package com.nashtech.AssetManagement_backend.controller;

import com.nashtech.AssetManagement_backend.dto.ReportDTO;
import com.nashtech.AssetManagement_backend.dto.StateQuantity;
import com.nashtech.AssetManagement_backend.repository.AssetRepository;
import com.nashtech.AssetManagement_backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    ReportService reportService;

    @Autowired
    AssetRepository assetRepository;

    @GetMapping
    public List<ReportDTO> getReport(HttpServletRequest request) {
        return reportService.getReport(request.getAttribute("userName").toString());
    }
}
