package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.site;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SiteUpdateRequest {

    @NotNull(message = "Field must not be null")
    @NotBlank(message = "Field must not be blank")
    private String name;

    @NotNull(message = "Field must not be null")
    @NotBlank(message = "Field must not be blank")
    private String address;

    private String note;

    private boolean enabled;
}
