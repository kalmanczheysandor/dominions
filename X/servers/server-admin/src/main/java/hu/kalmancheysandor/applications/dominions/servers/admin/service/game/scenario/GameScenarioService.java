package hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario;


import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game.GameScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.proxy.game.LizAiServerProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game.GameScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game.GameSessionRepository;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.game.scenario.*;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception.GameScenarioNotFoundException;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception.GameScenarioReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception.GameScenarioTitleIsReservedException;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception.GameScenarioNotFoundByUuidException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class GameScenarioService {

    @Autowired
    private GameScenarioRepository gameScenarioRepository;

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private LizAiServerProxy lizAiServerProxy;

    private String getMainImagesFolder() {
        return applicationConfig.getAdminServer().getGameScenario().getMainImagePath();
    }

    public FileHandler.Result image(@NotBlank String gameScenarioUuid) {

        // Access entity via repository
        GameScenario gameScenario = gameScenarioRepository.findByUuid(gameScenarioUuid);
        if (gameScenario == null) {
            throw new GameScenarioNotFoundByUuidException(gameScenarioUuid);
        }

        // Access image file
        int gameScenarioId = gameScenario.getId();
//        String fullPath = "d:/gameScenarios/" + gameScenarioId + "/main.jpg";
        String fullPath = getMainImagesFolder() + "/" + gameScenarioId + "/main.jpg";
        System.out.println("FULL PATH: " + fullPath);
        return FileHandler.accessFileContentIfExists(fullPath);
    }

    public GameScenarioAccessResponse accessGameScenario(@NotBlank String gameScenarioUuid) {

        // Access entity via repository
        GameScenario gameScenario = gameScenarioRepository.findByUuid(gameScenarioUuid);
        if (gameScenario == null) {
            throw new GameScenarioNotFoundByUuidException(gameScenarioUuid);
        }

        // Generate response
        GameScenarioAccessResponse response = modelMapper.map(gameScenario, GameScenarioAccessResponse.class);
        return response;
    }

    public List<GameScenarioItemResponse> listAllGameScenario() {

        // Access entity via repository
        List<GameScenario> gameScenarioList = gameScenarioRepository.findAll();

        // Generate response
        return gameScenarioList.stream()
                .map(item -> modelMapper.map(item, GameScenarioItemResponse.class))
                .collect(Collectors.toList());
    }

    public GameScenarioCreateResponse saveGameScenario(@NotNull GameScenarioCreateRequest request) {

        // Checking: Whether the new prn is reserved
        if (gameScenarioRepository.isTitleReserved(request.getTitle())) {
            throw new GameScenarioTitleIsReservedException(request.getTitle());
        }

        // Save gameScenario
        GameScenario gameScenarioToSave = new GameScenario();
        gameScenarioToSave.setTitle(request.getTitle());
        gameScenarioToSave.setDifficulty(request.getDifficulty());
        gameScenarioToSave.setDescription(request.getDescription());
        gameScenarioToSave.setGameMap(request.getGameMap());
        gameScenarioToSave.setEnabled(request.isEnabled());
        gameScenarioToSave.setPublished(request.isPublished());
        GameScenario gameScenarioSaved = uuidGenerator.saveWithRetry(gameScenarioRepository, gameScenarioToSave);

        // Save image
//        String filename = "d:/gameScenarios/" + gameScenarioSaved.getId() + "/main.jpg";
        String filename = getMainImagesFolder() + "/" + gameScenarioSaved.getId() + "/main.jpg";
        System.out.println("image path:"+filename);
        FileHandler.saveBase64Image(filename, request.getImageBase64());

        // Generate response
        return modelMapper.map(gameScenarioSaved, GameScenarioCreateResponse.class);
    }

    public GameScenarioUpdateResponse updateGameScenario(@NotBlank String gameScenarioUuid, @NotNull GameScenarioUpdateRequest request) {

        // Access entity via repository
        GameScenario gameScenarioToModify = gameScenarioRepository.findByUuid(gameScenarioUuid);
        if (gameScenarioToModify == null) {
            throw new GameScenarioNotFoundByUuidException(gameScenarioUuid);
        }
        int gameScenarioId = gameScenarioToModify.getId();

        // Checking: Whether the new prn is reserved
        if (gameScenarioRepository.isTitleReserved(request.getTitle(), gameScenarioId)) {
            throw new GameScenarioTitleIsReservedException(request.getTitle());
        }

        // Modify gameScenario
        gameScenarioToModify.setTitle(request.getTitle());
        gameScenarioToModify.setDifficulty(request.getDifficulty());
        gameScenarioToModify.setDescription(request.getDescription());
        gameScenarioToModify.setGameMap(request.getGameMap());
        gameScenarioToModify.setEnabled(request.isEnabled());
        gameScenarioToModify.setPublished(request.isPublished());
        GameScenario gameScenarioModified = uuidGenerator.saveWithRetry(gameScenarioRepository, gameScenarioToModify);

        // Modify image
        String filename = getMainImagesFolder() + "/" + gameScenarioModified.getId() + "/main.jpg";
        FileHandler.updateBase64Image(filename, request.getImageBase64());

        // Generate response
        return modelMapper.map(gameScenarioModified, GameScenarioUpdateResponse.class);
    }

    public void deleteOneGameScenario(@NotBlank String uuid) {

        // Execution
        try {
            this.deleteOneRow(uuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultipleGameScenario(@NotNull GameScenarioDeleteRequest request) {

        // Execution
        try {
            for (String uuid : request.getItems()) {
                this.deleteOneRow(uuid);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    private void deleteOneRow(@NotBlank String gameScenarioUuid) {

        // Access entity via repository
        GameScenario gameScenarioToDelete = gameScenarioRepository.findByUuid(gameScenarioUuid);
        if (gameScenarioToDelete == null) {
            throw new GameScenarioNotFoundByUuidException(gameScenarioUuid);
        }
        int gameScenarioId = gameScenarioToDelete.getId();


        // Delete all expired session, so parent table will contain less active references.
        gameSessionRepository.deleteAllExpired(LocalDateTime.now());    // GameSession table is where scenario id is used as a foreign key.

        // Check: whether any foreign key referencing this record
        if (gameScenarioRepository.isReferencedElsewhere(gameScenarioId)) {
            throw new GameScenarioReferencedElsewhereException(gameScenarioId, gameScenarioToDelete.getTitle());
        }

        // Delete item from db
        try {
            deleteScenarioAtAllAiPlayer(gameScenarioId);
            gameScenarioRepository.deleteById(gameScenarioId);
        } catch (EntityNotFoundException e) {
            throw new GameScenarioNotFoundException(gameScenarioId);
        }

        // Delete belonging files
        String folderPath = getMainImagesFolder() + "/" + gameScenarioId;
        FileHandler.deleteDirectoryRecursivelyIfExists(folderPath);
    }

    private void deleteScenarioAtAllAiPlayer(int gameScenarioId) {

        CompletableFuture<Void> a = CompletableFuture.runAsync(() -> lizAiServerProxy.deleteScenario(gameScenarioId));
        CompletableFuture.allOf(a).join();
    }

}