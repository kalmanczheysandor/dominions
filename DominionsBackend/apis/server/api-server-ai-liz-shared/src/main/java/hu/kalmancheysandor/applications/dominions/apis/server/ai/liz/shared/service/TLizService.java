package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.TAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.service.TAiService;
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

    protected String generateNetworkFileDirectoryString(LizNeuralTrainingTask task) {
        return applicationConfig.getLizServer().getNeuralNetwork().getBaseFolder() + "/trainings/concept-" + task.getConceptId() + "/scenario-" + task.getScenarioId() + "/player-" + task.getPlayerId();
    }



    protected static String generateNetworkFileName(LizNeuralTrainingTask task) {
        return "LizAi_Concept" + task.getConceptId() + "_Scenario" + task.getScenarioId() + "_Player" + task.getPlayerId() + "_Build" + task.getExecutionId() + ".nnet";
    }
}
