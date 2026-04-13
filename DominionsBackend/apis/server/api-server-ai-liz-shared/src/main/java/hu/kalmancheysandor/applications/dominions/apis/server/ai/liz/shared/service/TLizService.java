package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.TAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.service.TAiService;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingTask;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TLizService extends TAiService {
    @Autowired
    private ApplicationConfig applicationConfig;

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
                .cellCount(42)// TODO ez nem lehet fix
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


}
