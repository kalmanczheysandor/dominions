package hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game;


import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Table(name = "t_game_scenario")
@Data
public class GameScenario implements UUIDIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "title", unique = true, nullable = false)
    private String title;


    @Column(name = "difficulty", nullable = false)
    private String difficulty;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "game_map", nullable = false, columnDefinition = "JSON")
    private String gameMap;



    @Column(name = "player_human_count", nullable = false)
    private int playerHumanCount;

    @Column(name = "player_ai_count", nullable = false)
    private int playerAiCount;


    @Column(name = "is_published", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean published;

    @Column(name = "is_enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameScenario that)) return false;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    @Override
    public String toString() {
        return "GameScenario{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", description='" + description + '\'' +
                ", gameMap='" + gameMap + '\'' +
                ", playerHumanCount=" + playerHumanCount +
                ", playerAiCount=" + playerAiCount +
                ", published=" + published +
                ", enabled=" + enabled +
                '}';
    }
}
