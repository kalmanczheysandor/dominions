package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMapPlayerType;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action.GameAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "j-type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HumanPlayer.class, name = "HumanPlayer"),
    @JsonSubTypes.Type(value = ArtificialPlayer.class, name = "ArtificialPlayer")
})
public abstract class PlayerData {
    private int index;
    private GameMapPlayerType playerType;
    private String name;
    private GameAction intention = null;
    private boolean intentionGiven = false;
    private String endpointKey;
    private String userUuid;



    public PlayerData(int index, @NotBlank String userUuid, @NotBlank String name, @NotNull GameMapPlayerType gameMapPlayerType, @NotBlank String endpointKey) {
        this.index = index;
        this.name = name;
        this.userUuid = userUuid;
        this.playerType = gameMapPlayerType;
        this.endpointKey = endpointKey;
    }

    @JsonIgnore
    public boolean isIntentionAlreadyGiven() {
        return intentionGiven;
    }

    @JsonIgnore
    public void flushIntention() {
        intentionGiven = false;
        intention = null;
    }

    @JsonIgnore
    public boolean isArtificial() {
        if(playerType == GameMapPlayerType.ARTIFICIAL) {
            return true;
        }
        return false;
    }
    @JsonIgnore
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerData that = (PlayerData) o;
        return index == that.index;
    }
    @JsonIgnore
    @Override
    public int hashCode() {
        return Objects.hashCode(index);
    }

}
