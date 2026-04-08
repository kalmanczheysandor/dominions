package hu.kalmancheysandor.applications.dominions.servers.auth.dto.site;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteAuthenticationSuccessResponse {
    private String sessionId;
    private String identifier;
    private String userUuid;
    private List<String> permissions;
    private String endpoint;
}

