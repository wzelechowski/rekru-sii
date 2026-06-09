package rekru.sii.report.service;

import rekru.sii.report.payload.response.ReportResponse;

import java.util.List;

public interface ReportService {
    List<ReportResponse> getReport();
}
