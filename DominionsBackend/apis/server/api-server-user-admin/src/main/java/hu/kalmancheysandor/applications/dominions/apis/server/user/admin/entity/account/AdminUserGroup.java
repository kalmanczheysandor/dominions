package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;


@Entity
@IdClass(AdminUserGroup.PrimaryKey.class)
@Table(name = "t_admin_user_group")
@Data
@NoArgsConstructor
public class AdminUserGroup {


    public AdminUserGroup(Integer userId, Integer groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    @Id
    @Column(name="user_id", nullable=false)
    private Integer userId;

    @Id
    @Column(name="group_id", nullable=false)
    private Integer groupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("groupId")
    @JoinColumn(name = "group_id",referencedColumnName = "id")
    private AdminUserAuthorisationGroup groupDetails;

//    @Embeddable
    public static class PrimaryKey implements Serializable {
        private Integer userId;
        private Integer groupId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(userId, that.userId) && Objects.equals(groupId, that.groupId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, groupId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminUserGroup adminUserGroup = (AdminUserGroup) o;
        return Objects.equals(userId, adminUserGroup.userId) && Objects.equals(groupId, adminUserGroup.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }
}
