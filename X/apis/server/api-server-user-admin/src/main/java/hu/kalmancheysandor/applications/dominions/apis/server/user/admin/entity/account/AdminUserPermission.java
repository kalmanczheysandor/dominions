package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(AdminUserPermission.PrimaryKey.class)
@Table(name = "v_admin_user_permission")
@Data
@Setter(AccessLevel.NONE) // Prevent Lombok from generating setters
public class AdminUserPermission {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Id
    @Column(name = "permission_target", nullable = false, unique = true)
    private String permissionTarget;

    @Column(name = "permission_is_view", columnDefinition = "INT", nullable = false)
    private boolean permissionIsView;

    @Column(name = "permission_is_add", columnDefinition = "INT", nullable = false)
    private boolean permissionIsAdd;

    @Column(name = "permission_is_edit", columnDefinition = "INT", nullable = false)
    private boolean permissionIsEdit;

    @Column(name = "permission_is_delete", columnDefinition = "INT", nullable = false)
    private boolean permissionIsDelete;


    @Override
    public String toString() {
        return "UserPermission{" +
            "userId=" + userId +
            ", permissionTarget='" + permissionTarget + '\'' +
            ", permissionIsView=" + permissionIsView +
            ", permissionIsAdd=" + permissionIsAdd +
            ", permissionIsEdit=" + permissionIsEdit +
            ", permissionIsDelete=" + permissionIsDelete +
            '}';
    }

//    @Embeddable
    public static class PrimaryKey implements Serializable {
        private Integer userId;
        private String permissionTarget;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(userId, that.userId) && Objects.equals(permissionTarget, that.permissionTarget);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, permissionTarget);
        }
    }
}
