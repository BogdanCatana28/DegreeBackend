package fortech.team2.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * It was absolutelly necessary to create a dto with only one field ?
 */
public class TokenRefreshRequest {
    private String refreshToken;
}