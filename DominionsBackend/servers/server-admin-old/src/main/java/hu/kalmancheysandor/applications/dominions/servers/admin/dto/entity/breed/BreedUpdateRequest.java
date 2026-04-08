package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.breed;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BreedUpdateRequest {

    @NotNull(message = "Field must not be null")
    @NotBlank(message = "Field must not be blank")
    private String name;

    private boolean enabled;
}
