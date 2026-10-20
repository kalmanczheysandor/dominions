package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service;

import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.TAiAlgorithm;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.service.TAiService;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistorySession;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingTask;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.LizNeuralConceptDirectoryDeletionFailedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history.LizHistoryPlayerNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history.LizHistoryScenarioNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history.LizHistorySessionNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistorySessionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public abstract class TLizService extends TAiService {
    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private LizHistoryScenarioRepository lizHistoryScenarioRepository;

    @Autowired
    private LizHistoryPlayerRepository lizHistoryPlayerRepository;


    @Autowired
    private LizHistorySessionRepository lizHistorySessionRepository;

    protected static LizNeuralNetwork.Configuration generateNetworkConfiguration() {
        return LizNeuralNetwork.Configuration.builder()
                .enemiesCountMin(TAiAlgorithm.ENEMIES_COUNT_MIN)
                .enemiesCountMax(TAiAlgorithm.ENEMIES_COUNT_MAX)
                .ownerRankMinIndex(TAiAlgorithm.OWNER_RANKS_INDEX_MIN)
                .ownerRankMaxIndex(TAiAlgorithm.OWNER_RANKS_INDEX_MAX)
                .defendersMinCount(TAiAlgorithm.DEFENDERS_COUNT_MIN)
                .defendersMaxCount(TAiAlgorithm.DEFENDERS_COUNT_MAX)
                .attackPowerMin(TAiAlgorithm.RESERVE_SIZE_MIN)
                .attackPowerMax(TAiAlgorithm.RESERVE_SIZE_MAX)
                .cellCount(TAiAlgorithm.CELL_C0UNT)
                .build();
    }

    protected String generateNetworkFileDirectoryStringByTrainingTask(LizNeuralTrainingTask task) {
        return generateNetworkFileDirectoryString(task.getConceptId(), task.getScenarioId(), task.getPlayerId());
    }

    protected String generateNetworkFileDirectoryStringByTrainingResult(LizNeuralTrainingResult trainingResult) {
        return generateNetworkFileDirectoryString(trainingResult.getConceptId(), trainingResult.getScenarioId(), trainingResult.getPlayerId());
    }

    protected String generateNetworkFileDirectoryString(int conceptId, int scenarioId, int playerId) {
        return generateConceptDirectoryString(conceptId) + "/scenario-" + scenarioId + "/player-" + playerId;
    }

    private String generateConceptDirectoryString(int conceptId) {
        return applicationConfig.getLizServer().getNeuralNetwork().getBaseFolder() + "/trainings/concept-" + conceptId;
    }


    protected void deleteRecursivelyConceptDirectoryIfExists(int conceptId) {
        try {
            String pathToConceptFolder = generateConceptDirectoryString(conceptId);
            Path path = Paths.get(pathToConceptFolder);

            if (Files.exists(path)) {
                Files.walk(path)
                        .sorted(Comparator.reverseOrder()) // It is important in order to delete files earlier than folders
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                               throw new LizNeuralConceptDirectoryDeletionFailedException(conceptId);
                            }

                        });
            }
        } catch (IOException e) {
            throw new LizNeuralConceptDirectoryDeletionFailedException(conceptId);
        }
    }


    protected static String generateNetworkFileNameByTrainingTask(LizNeuralTrainingTask task) {
        return generateNetworkFileName(task.getConceptId(), task.getScenarioId(), task.getPlayerId(), task.getExecutionId());
    }

    protected static String generateNetworkFileNameByTrainingResult(LizNeuralTrainingResult trainingResult) {
        return generateNetworkFileName(trainingResult.getConceptId(), trainingResult.getScenarioId(), trainingResult.getPlayerId(), trainingResult.getExecutionId());
    }

    protected static String generateNetworkFileName(int conceptId, int scenarioId, int playerId, int executionId) {
        return "LizAi_Concept" + conceptId + "_Scenario" + scenarioId + "_Player" + playerId + "_Build" + executionId + ".nnet";
    }


    protected void registerHistoryScenarioIfNotExists(String scenarioUuid, String scenarioName) {
        // Determine current record and its id
        LizHistoryScenario lizHistoryScenario = lizHistoryScenarioRepository.findByScenarioUuid(scenarioUuid);
        if (lizHistoryScenario == null) { // Register it if it was not
            lizHistoryScenarioRepository.save(new LizHistoryScenario(scenarioUuid, scenarioName));
        }
    }


    protected void registerHistoryPlayerIfNotExists(String userUuid, String userName) {
        // Determine current record and its id
        LizHistoryPlayer lizHistoryPlayer = lizHistoryPlayerRepository.findByUserUuid(userUuid);
        if (lizHistoryPlayer == null) { // Register it if it was not
            lizHistoryPlayerRepository.save(new LizHistoryPlayer(userUuid, userName));
        }
    }

    protected void registerHistorySessionIfNotExists(String sessionUuid) {
        // Determine current record and its id
        LizHistorySession lizHistorySession = lizHistorySessionRepository.findBySessionUuid(sessionUuid);
        if (lizHistorySession == null) { // Register it if it was not
            lizHistorySessionRepository.save(new LizHistorySession(sessionUuid));
        }
    }

    protected LizHistoryScenario accessHistoryScenario(String scenarioUuid) {

        LizHistoryScenario lizHistoryScenario = lizHistoryScenarioRepository.findByScenarioUuid(scenarioUuid);
        if (lizHistoryScenario == null) {
            throw new LizHistoryScenarioNotFoundByUuidException(scenarioUuid);
        }
        return lizHistoryScenario;
    }


    protected LizHistoryPlayer accessHistoryPlayer(String userUuid) {
        LizHistoryPlayer lizHistoryPlayer = lizHistoryPlayerRepository.findByUserUuid(userUuid);
        if (lizHistoryPlayer == null) {
            throw new LizHistoryPlayerNotFoundByUuidException(userUuid);
        }
        return lizHistoryPlayer;
    }

    protected LizHistorySession accessHistorySession(String sessionUuid) {
        LizHistorySession lizHistorySession = lizHistorySessionRepository.findBySessionUuid(sessionUuid);
        if (lizHistorySession == null) {
            throw new LizHistorySessionNotFoundByUuidException(sessionUuid);
        }
        return lizHistorySession;
    }
}
