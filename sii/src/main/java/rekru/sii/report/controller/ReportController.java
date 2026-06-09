package rekru.sii.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rekru.sii.report.payload.response.ReportResponse;
import rekru.sii.report.service.ReportService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponse>> getReport() {
        return ResponseEntity.ok(reportService.getReport());
    }
}
