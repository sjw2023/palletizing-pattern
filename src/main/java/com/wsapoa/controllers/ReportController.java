//package com.wsapoa.controllers;
//
//import com.wsapoa.dto.ReportRequestDTO;
//import com.wsapoa.entity.ReportResult;
//import com.wsapoa.services.ReportService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/reports")
//@RequiredArgsConstructor
//public class ReportController {
//    private final ReportService reportService;
//    @PostMapping
//    public ResponseEntity<ReportResult> createReport(@RequestBody ReportRequestDTO reportRequestDTO) {
//        var result = reportService.createReport(reportRequestDTO);
//        return ResponseEntity.ok(result);
//    }
//}
package com.wsapoa.controllers;

import com.wsapoa.dto.ReportRequestDTO;
import com.wsapoa.entity.ReportResult;
import com.wsapoa.services.BaseService;
import com.wsapoa.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController extends BaseController<ReportResult, Long, ReportRequestDTO> {

    private final ReportService reportService;

    @Override
    protected BaseService<ReportResult, Long, ReportRequestDTO> getService() {
        return reportService;
    }
}