package hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game;



import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_game_session")
@Data
public class GameSession implements UUIDIdentifiable {

    @Id
    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "scenario_id", nullable = false)
    private int scenarioId;

    @Column(name = "play_state", nullable = false, columnDefinition = "JSON")
    private String playState;

    @Column(name = "game_map", nullable = false, columnDefinition = "JSON")
    private String gameMap;

    @Column(name = "free_slot_count", nullable = false)
    private int freeSlotCount;

    @Column(name = "is_recruiting", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean recruiting;

    @Column(name = "is_visible", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean visible;

    @Column(name = "is_enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameSession that)) return false;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
