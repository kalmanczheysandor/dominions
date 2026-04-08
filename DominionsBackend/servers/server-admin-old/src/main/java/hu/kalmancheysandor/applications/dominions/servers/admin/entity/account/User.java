package hu.kalmancheysandor.applications.dominions.servers.admin.entity.account;

import hu.kalmancheysandor.applications.dominions.servers.admin.utils.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;


@Entity
@Table(name = "t_user")
@Data
public class User implements UUIDIdentifiable {

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

    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
    private List<UserGroup> userGroups;


    public UserGroup findUserGroupWithGroupId(int groupId) {
        if(userGroups!=null){
            for(UserGroup userGroup:userGroups){
                if(userGroup.getGroupId()==groupId){
                    return userGroup;
                }
            }
        }
        return null;
    }

    public boolean existsUserGroupWithGroupId(int groupId) {
        if(userGroups!=null){
            for(UserGroup userGroup:userGroups){
                if(userGroup.getGroupId()==groupId){
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
        User user = (User) o;
        return Objects.equals(id, user.id);
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
