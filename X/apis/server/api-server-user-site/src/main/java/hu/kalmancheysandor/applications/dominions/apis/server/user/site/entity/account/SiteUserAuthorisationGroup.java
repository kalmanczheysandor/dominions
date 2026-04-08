package hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account;


import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Entity
@Table(name = "t_site_user_authorisation_group")
@Data
public class SiteUserAuthorisationGroup implements UUIDIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "name", nullable = false,unique = true)
    private String name;

    @Column(name = "is_enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

//    @OneToMany(mappedBy = "groupId", fetch = FetchType.LAZY)
//    private List<SiteUserAuthorisationGroupPermission> permissionList;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<SiteUserAuthorisationGroupPermission> permissionList;

    @Override
    public String toString() {
        return "SiteUserAuthorisationGroup{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", enabled=" + enabled +
                ", permissionList=" + permissionList +
                '}';
    }
}
