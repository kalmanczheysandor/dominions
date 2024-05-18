package hu.kalmancheysandor.applications.dominion.server.web.controller.user.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateFormData {
    private Integer id;
    private String identifier;
    private String password;
    private String name;
    private Boolean rolePlayer;
    private Boolean roleAdmin;
    private Boolean roleEngineer;
}
