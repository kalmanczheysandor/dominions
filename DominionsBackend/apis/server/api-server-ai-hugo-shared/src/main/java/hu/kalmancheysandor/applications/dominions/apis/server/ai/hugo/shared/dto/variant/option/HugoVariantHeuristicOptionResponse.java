package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant.option;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HugoVariantHeuristicOptionResponse {
    private String uuid;
    private String name;
}
