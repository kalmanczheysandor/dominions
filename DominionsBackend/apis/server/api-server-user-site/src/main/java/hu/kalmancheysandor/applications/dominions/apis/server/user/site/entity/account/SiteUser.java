package hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account;

import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


@Entity
@Table(name = "t_site_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUser implements UUIDIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "identifier", unique = true, nullable = false)
    private String identifier;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @Column(name = "is_finalised", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean finalised;

//    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
//    private List<SiteUserGroup> siteUserGroups;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<SiteUserGroup> siteUserGroups;


    public SiteUserGroup findUserGroupWithGroupId(int groupId) {
        if(siteUserGroups !=null){
            for(SiteUserGroup siteUserGroup : siteUserGroups){
                if(siteUserGroup.getGroupId()==groupId){
                    return siteUserGroup;
                }
            }
        }
        return null;
    }

    public boolean existsUserGroupWithGroupId(int groupId) {
        if(siteUserGroups !=null){
            for(SiteUserGroup siteUserGroup : siteUserGroups){
                if(siteUserGroup.getGroupId()==groupId){
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteUser siteUser = (SiteUser) o;
        return Objects.equals(id, siteUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", identifier='" + identifier + '\'' +
            ", password='" + password + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
