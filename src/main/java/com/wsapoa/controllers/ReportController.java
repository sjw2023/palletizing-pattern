package com.wsapoa.controllers;

import com.wsapoa.dto.ReportRequestDTO;
import com.wsapoa.entity.ReportResult;
import com.wsapoa.services.BaseService;
import com.wsapoa.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController extends BaseController<ReportResult, Long, ReportRequestDTO> {

    private final ReportService reportService;

    //TODO : Change is to create and request get method
    @PostMapping("/createReport")
    public ResponseEntity<List<ReportResult>> createReport(@RequestBody ReportRequestDTO reportRequestDTO) {
        List<ReportResult> results = reportService.createReport(reportRequestDTO);
        return ResponseEntity.ok(results);
    }

    @Override
    protected BaseService<ReportResult, Long, ReportRequestDTO> getService() {
        return reportService;
    }
}