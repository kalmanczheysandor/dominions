package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.breed;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class BreedCreateRequest {

    @NotNull(message = "Field must not be null")
    @NotBlank(message = "Field must not be blank")
    private String name;

    private boolean enabled;
}
