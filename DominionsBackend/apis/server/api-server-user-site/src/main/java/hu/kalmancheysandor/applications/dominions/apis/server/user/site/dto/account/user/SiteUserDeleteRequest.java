package hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.user;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUserDeleteRequest {

    @NotNull(message = "Field must not be null!")
    @NotEmpty(message = "Field must not be empty!")
    private String[] items;
}
