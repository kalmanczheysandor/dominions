package hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training;


import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "t_ai_liz_training")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LizTraining implements UUIDIdentifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "name", unique = true, nullable = false, updatable = false)
    private String name;

    @Column(name="scenario_id", nullable=false)
    private Integer scenarioId;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

}