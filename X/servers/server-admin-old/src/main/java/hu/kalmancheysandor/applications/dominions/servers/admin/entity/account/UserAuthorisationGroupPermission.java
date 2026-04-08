package hu.kalmancheysandor.applications.dominions.servers.admin.entity.account;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(UserAuthorisationGroupPermission.PrimaryKey.class)
@Table(name = "t_user_authorisation_group_permission")
@Data
@NoArgsConstructor
public class UserAuthorisationGroupPermission {

    @Id
    @Column(name = "group_id", nullable = false)
    private Integer groupId;

    @Id
    @Column(name = "target", nullable = false, unique = true)
    private String target;

    @Column(name = "permission_is_view", columnDefinition = "TINYINT(1) UNSIGNED", nullable = false)
    private boolean permissionIsView;

    @Column(name = "permission_is_add", columnDefinition = "TINYINT(1) UNSIGNED", nullable = false)
    private boolean permissionIsAdd;

    @Column(name = "permission_is_edit", columnDefinition = "TINYINT(1) UNSIGNED", nullable = false)
    private boolean permissionIsEdit;

    @Column(name = "permission_is_delete", columnDefinition = "TINYINT(1) UNSIGNED", nullable = false)
    private boolean permissionIsDelete;

//    @ManyToOne
//    @JoinColumn(name = "group_id")
//    private UserAuthorisationGroup group;


    public UserAuthorisationGroupPermission(String target,Integer groupId,  boolean permissionIsView, boolean permissionIsAdd, boolean permissionIsEdit, boolean permissionIsDelete) {
        this.groupId = groupId;
        this.target = target;
        this.permissionIsView = permissionIsView;
        this.permissionIsAdd = permissionIsAdd;
        this.permissionIsEdit = permissionIsEdit;
        this.permissionIsDelete = permissionIsDelete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthorisationGroupPermission that = (UserAuthorisationGroupPermission) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, target);
    }

    @Embeddable
    public static class PrimaryKey implements Serializable {
        private Integer groupId;
        private String target;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(groupId, that.groupId) && Objects.equals(target, that.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(groupId, target);
        }
    }
}
