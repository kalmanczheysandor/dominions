package hu.kalmancheysandor.applications.dominions.servers.admin.entity.account;

import hu.kalmancheysandor.applications.dominions.servers.admin.utils.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "t_user_authorisation_group")
@Data
public class UserAuthorisationGroup implements UUIDIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="uuid",unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="is_enabled",nullable = false,columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @OneToMany(mappedBy="groupId",fetch = FetchType.LAZY)
    private List<UserAuthorisationGroupPermission> permissionList;
}
