package hu.kalmancheysandor.applications.dominion.server.web.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Entity
@Table(name = "t_user_role")
@Data
@NoArgsConstructor
public class UserRole implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public UserRole(String authority, User user) {
        this.authority = authority;
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(authority, userRole.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority);
    }
}
