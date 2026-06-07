package rekru.sii.gym.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rekru.sii.gym.payload.response.ReportResponse;
import rekru.sii.gym.repository.GymRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {
    private final GymRepository gymRepository;

    @Override
    public List<ReportResponse> getReport() {
        return gymRepository.getReport();
    }
}
