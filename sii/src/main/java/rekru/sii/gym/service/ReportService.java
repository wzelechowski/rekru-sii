package rekru.sii.gym.service;

import rekru.sii.gym.payload.response.ReportResponse;

import java.util.List;

public interface ReportService {
    List<ReportResponse> getReport();
}
