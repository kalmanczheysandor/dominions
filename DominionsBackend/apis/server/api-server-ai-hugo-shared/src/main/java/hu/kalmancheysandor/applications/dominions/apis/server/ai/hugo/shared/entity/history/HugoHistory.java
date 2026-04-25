package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.history;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@IdClass(HugoHistory.PrimaryKey.class)
@Table(name = "t_ai_hugo_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HugoHistory {

    @Id
    @Column(name="session_id", nullable=false)
    private Integer sessionId;

    @Id
    @Column(name="turn", nullable=false)
    private Integer turn;

    @Id
    @Column(name="player_id", nullable=false)
    private Integer playerId;

    @Column(name="scenario_id", nullable=false)
    private Integer scenarioId;


    @Column(columnDefinition = "TEXT")
    private String decision;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;


    public static class PrimaryKey implements Serializable {
        private Integer sessionId;
        private Integer turn;
        private Integer playerId;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(sessionId, that.sessionId) && Objects.equals(turn, that.turn) && Objects.equals(playerId, that.playerId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sessionId, turn, playerId);
        }
    }
}