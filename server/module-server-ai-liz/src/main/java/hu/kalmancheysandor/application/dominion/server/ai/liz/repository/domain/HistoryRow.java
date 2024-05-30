package hu.kalmancheysandor.application.dominion.server.ai.liz.repository.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "t_game_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String player;
    @Column(columnDefinition = "TEXT")
    private String decision;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerDecisionCell {
        private int reserveSize = 0;
        private int enemiesCount = 0;
        private Map<Integer, Field> fields = new HashMap<>();
        private Move decision;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Field {
            public Integer playerKey;
            public int defenders;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Move {
            private int target;
            private int troops;
        }
    }
}