package hu.kalmancheysandor.applications.dominion.server.web.service.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateRequest {
    private Long id;
    private String identifier;
    private String password;
    private String name;
    private Boolean rolePlayer;
    private Boolean roleAdmin;
    private Boolean roleEngineer;
}
