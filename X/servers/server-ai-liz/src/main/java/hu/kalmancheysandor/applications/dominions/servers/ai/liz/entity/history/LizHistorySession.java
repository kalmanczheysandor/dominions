package hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.history;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_ai_liz_history_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LizHistorySession {

    public LizHistorySession(String sessionUuid) {
        this.sessionUuid = sessionUuid;
        this.dateCreated = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_uuid", unique = true, nullable = false, updatable = false)
    private String sessionUuid;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LizHistorySession that = (LizHistorySession) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
