package hu.kalmancheysandor.applications.dominions.apis.util.configuration;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameServer {
        private SessionManagement sessionManagement;
        @Data
        @NoArgsConstructor
        public static class SessionManagement {
            @Min(1)
            private int expirationHours;
        }
}
