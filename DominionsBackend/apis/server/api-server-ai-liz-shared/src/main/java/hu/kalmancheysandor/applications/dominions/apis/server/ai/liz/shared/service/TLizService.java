package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.TAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.service.TAiService;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistorySession;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingTask;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history.LizHistoryPlayerNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history.LizHistoryScenarioNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history.LizHistorySessionNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistorySessionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;

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
                .enemiesCountMin(TAiEngine.ENEMIES_COUNT_MIN)
                .enemiesCountMax(TAiEngine.ENEMIES_COUNT_MAX)
                .ownerRankMinIndex(TAiEngine.OWNER_RANKS_INDEX_MIN)
                .ownerRankMaxIndex(TAiEngine.OWNER_RANKS_INDEX_MAX)
                .defendersMinCount(TAiEngine.DEFENDERS_COUNT_MIN)
                .defendersMaxCount(TAiEngine.DEFENDERS_COUNT_MAX)
                .attackPowerMin(TAiEngine.RESERVE_SIZE_MIN)
                .attackPowerMax(TAiEngine.RESERVE_SIZE_MAX)
                .cellCount(TAiEngine.CELL_C0UNT)
                .build();
    }

    protected String generateNetworkFileDirectoryStringByTrainingTask(LizNeuralTrainingTask task) {
        return generateNetworkFileDirectoryString(task.getConceptId(), task.getScenarioId(), task.getPlayerId());
    }

    protected String generateNetworkFileDirectoryStringByTrainingResult(LizNeuralTrainingResult trainingResult) {
        return generateNetworkFileDirectoryString(trainingResult.getConceptId(), trainingResult.getScenarioId(), trainingResult.getPlayerId());
    }

    protected String generateNetworkFileDirectoryString(int conceptId, int scenarioId, int playerId) {
        return applicationConfig.getLizServer().getNeuralNetwork().getBaseFolder() + "/trainings/concept-" + conceptId + "/scenario-" + scenarioId + "/player-" +playerId;
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


    protected void registerHistoryPlayerIfNotExists(String userUuid,String userName) {
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
