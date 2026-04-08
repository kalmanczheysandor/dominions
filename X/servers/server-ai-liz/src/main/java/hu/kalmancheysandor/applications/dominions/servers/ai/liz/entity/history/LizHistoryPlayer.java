package hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.history;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_ai_liz_history_player")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LizHistoryPlayer {

    public LizHistoryPlayer(String userUuid) {
        this.userUuid = userUuid;
        this.dateCreated = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_uuid", unique = true, nullable = false, updatable = false)
    private String userUuid;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LizHistoryPlayer that = (LizHistoryPlayer) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
