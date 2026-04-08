package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class DogCreateRequest {

    @NotBlank(message = "Field must not be blank")
    private String prn;

    @NotBlank(message = "Field must not be blank")
    private String name;

    @NotBlank(message = "Field must not be blank")
    private String breedUuid;

    @NotBlank(message = "Field must not be blank")
    private String siteUuid;

    private String note;

    private boolean enabled;

    private String imageBase64;
}
