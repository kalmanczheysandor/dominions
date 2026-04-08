package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DogImageUploadRequest {
    @NotBlank
    private String imageBase64;

    @NotBlank
    private String filename;
}
