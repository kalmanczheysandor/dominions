package hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GameMapConfiguration implements Serializable {
    private Map<Integer, GameMapConfigurationPlayer> players;
    private Map<Integer, GameMapConfigurationBoardCell> boardCells;
    private GameMapConfigurationView view;

    public GameMapConfigurationPlayer addPlayer(int index, GameMapConfigurationPlayer gameMapConfigurationPlayer) {
        if (this.players == null) {
            this.players = new HashMap<>();
        }
        return this.players.put(index, gameMapConfigurationPlayer);
    }

    public GameMapConfigurationBoardCell addBoardCell(int index, GameMapConfigurationBoardCell gameMapConfigurationBoardCell) {

        if (this.boardCells == null) {
            this.boardCells = new HashMap<>();
        }

        return this.boardCells.put(index, gameMapConfigurationBoardCell);
    }

    public int getPlayerCount() {
        return this.players != null ? this.players.size() : 0;
    }

    public int getCellCount() {
        return this.boardCells != null ? this.boardCells.size() : 0;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// [ Sub classes ] //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Data
    @NoArgsConstructor
    @JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
    )
    public static class GameMapConfigurationView implements Serializable {

        private Background background;
        private Foreground foreground;

        @Data
        @NoArgsConstructor
        @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.NONE
        )
        public static class Background implements Serializable {

            private List<Meridian> meridians;
            private List<Parallel> parallels;

            @Data
            @NoArgsConstructor
            @JsonAutoDetect(
                fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE
            )
            public static class Meridian implements Serializable {
                private String x1;
                private String y1;
                private String x2;
                private String y2;
            }

            @Data
            @NoArgsConstructor
            @JsonAutoDetect(
                fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE
            )
            public static class Parallel implements Serializable {
                private String x1;
                private String y1;
                private String x2;
                private String y2;
            }

        }

        @Data
        @NoArgsConstructor
        @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.NONE
        )
        public static class Foreground implements Serializable {

            private List<Continent> continents;
            private List<Country> countries;
            private List<CountryLabel> countryLabels;
            private List<JumpLine> jumpLines;


            @Data
            @NoArgsConstructor
            @JsonAutoDetect(
                fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE
            )
            public static class Continent implements Serializable {
                private String note;
                private String points;
            }

            @Data
            @NoArgsConstructor
            @JsonAutoDetect(
                fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE
            )
            public static class Country implements Serializable {
                private String note;
                private int countryKey;
                private String points;
            }

            @Data
            @NoArgsConstructor
            @JsonAutoDetect(
                fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE
            )
            public static class CountryLabel implements Serializable {
                private int labelKey;
                private int ownerCountryKey;
                private String x;
                private String y;
                private int armySize;
            }

            @Data
            @NoArgsConstructor
            @JsonAutoDetect(
                fieldVisibility = JsonAutoDetect.Visibility.ANY,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE
            )
            public static class JumpLine implements Serializable {
                private String x1;
                private String y1;
                private String x2;
                private String y2;
            }

        }
    }

}
