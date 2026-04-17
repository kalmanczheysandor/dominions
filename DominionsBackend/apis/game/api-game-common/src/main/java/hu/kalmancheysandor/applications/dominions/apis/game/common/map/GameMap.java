package hu.kalmancheysandor.applications.dominions.apis.game.common.map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration.GameMapConfiguration;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.exception.UnableToParseJsonContentGameMapException;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.state.GameMapState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GameMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("mapName")
    private String mapName;

    @JsonProperty("configuration")
    private GameMapConfiguration configuration;

    @JsonProperty("initialState")
    private GameMapState initialState;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  Factory methods  ////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static GameMap createByJsonFile(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File(path);
            return objectMapper.readValue(jsonFile, GameMap.class);
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static GameMap createByJsonContent(String jsonContent) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonContent, GameMap.class);
        } catch (IOException e) {
            throw new UnableToParseJsonContentGameMapException();
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  ???? methods  //////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


}
