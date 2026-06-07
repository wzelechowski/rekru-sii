package rekru.sii.gym.payload.response;

import java.math.BigDecimal;

public record ReportResponse(
        String gymName,
        BigDecimal amount,
        String currency
) {
}
