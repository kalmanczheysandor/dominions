package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.history;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_ai_hugo_history_scenario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HugoHistoryScenario {

    public HugoHistoryScenario(String scenarioUuid, String caption) {
        this.scenarioUuid = scenarioUuid;
        this.dateCreated = LocalDateTime.now();
        this.caption = caption;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "scenario_uuid", unique = true, nullable = false, updatable = false)
    private String scenarioUuid;

    @Column(name = "caption", unique = true, nullable = false, updatable = false)
    private String caption;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HugoHistoryScenario that = (HugoHistoryScenario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
