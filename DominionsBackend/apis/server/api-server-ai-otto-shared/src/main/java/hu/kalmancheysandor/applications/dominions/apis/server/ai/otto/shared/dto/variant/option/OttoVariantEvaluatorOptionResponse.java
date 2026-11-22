package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.variant.option;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OttoVariantEvaluatorOptionResponse {
    private String uuid;
    private String name;
}
