package rekru.sii.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rekru.sii.report.payload.response.ReportResponse;
import rekru.sii.report.repository.ReportRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    @Override
    public List<ReportResponse> getReport() {
        return reportRepository.getReport();
    }
}
