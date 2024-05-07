package hu.kalmanczheysandor.applications.dominion.controller.user.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreateFormData {
    private Integer id;
    private String identifier;
    private String password;
    private String name;
    private Boolean rolePlayer;
    private Boolean roleAdmin;
    private Boolean roleEngineer;
}
