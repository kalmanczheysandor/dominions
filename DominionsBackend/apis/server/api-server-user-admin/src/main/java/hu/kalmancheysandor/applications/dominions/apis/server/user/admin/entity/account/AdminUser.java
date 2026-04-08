package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account;

import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


@Entity
@Table(name = "t_admin_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUser implements UUIDIdentifiable {

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
//    private List<AdminUserGroup> adminUserGroups;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<AdminUserGroup> adminUserGroups;

    public AdminUserGroup findUserGroupWithGroupId(int groupId) {
        if(adminUserGroups !=null){
            for(AdminUserGroup adminUserGroup : adminUserGroups){
                if(adminUserGroup.getGroupId()==groupId){
                    return adminUserGroup;
                }
            }
        }
        return null;
    }

    public boolean existsUserGroupWithGroupId(int groupId) {
        if(adminUserGroups !=null){
            for(AdminUserGroup adminUserGroup : adminUserGroups){
                if(adminUserGroup.getGroupId()==groupId){
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
        AdminUser adminUser = (AdminUser) o;
        return Objects.equals(id, adminUser.id);
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
