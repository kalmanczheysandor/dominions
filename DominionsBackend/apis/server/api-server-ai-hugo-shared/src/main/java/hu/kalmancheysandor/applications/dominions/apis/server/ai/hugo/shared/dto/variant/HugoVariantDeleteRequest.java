package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HugoVariantDeleteRequest {

    @NotNull(message = "Field must not be null!")
    @NotEmpty(message = "Field must not be empty!")
    private String[] items;
}
