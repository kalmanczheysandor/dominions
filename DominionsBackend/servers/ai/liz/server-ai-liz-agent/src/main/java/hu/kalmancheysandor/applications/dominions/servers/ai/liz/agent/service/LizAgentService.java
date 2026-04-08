package hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent.service;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;

import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.LizTrainingResultLatestRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service.TLizService;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class LizAgentService extends TLizService {

    @Autowired
    private LizHistoryPlayerRepository lizHistoryPlayerRepository;

    @Autowired
    private LizTrainingResultLatestRepository lizTrainingResultLatestRepository;


    public AiDecisionResponse generateResponse(AiDecisionRequest request) {
        int scenarioId = request.getScenarioId();

        // Initialise the decision-making engine
        INeuralAiEngine aiDecisionEngine = new LizAiEngine();

        // Register all enemy network
        for (Map.Entry<Integer, AiDecisionRequest.Player> playerEntry : request.getPlayers().entrySet()) {
            AiDecisionRequest.Player player = playerEntry.getValue();

            INeuralNetwork neuralNetwork = generateNetwork(player.getUserUuid(), scenarioId);
            aiDecisionEngine.registerEnemyNetwork(player.getUserUuid(), neuralNetwork);
        }

        // Make a decision
        AiDecisionContext context = convertRequestToDecisionContext(request);
        AiDecisionResult result = aiDecisionEngine.makeDecision(context);

        // Generate response
        return convertDecisionResultToResponse(result);
    }

    private INeuralNetwork generateNetwork(String userUuid, int scenarioId) {
        //
        LizHistoryPlayer lizHistoryPlayer = findPlayerAndRegisterIfNotExists(userUuid);
        int historyPlayerId = lizHistoryPlayer.getId();

        // Determine latest training,if no one exists than a blank is generated
        int latestTrainingId = 0;
        LizTrainingResultLatest latestTrainingLog = lizTrainingResultLatestRepository.findLatest(scenarioId, historyPlayerId);
        if (latestTrainingLog != null) {
            latestTrainingId = latestTrainingLog.getTrainingId();
        }

        //
        String directoryToStore = generateNetworkFileDirectoryString(scenarioId, historyPlayerId);
        FileHandler.createDirectoryIfNotExist(directoryToStore);

        // Generate network filepath
        String fileName = generateNetworkFileName(scenarioId, historyPlayerId, latestTrainingId);
        String filePath = directoryToStore + "/" + fileName;

        System.out.println("Network Generated: <scenarioId: " + scenarioId + ", historyPlayerId: " + historyPlayerId + ", latestTrainingId: " + latestTrainingId + ">");
        return new LizNeuralNetwork(generateNetworkConfiguration(), filePath);
    }


    private LizHistoryPlayer findPlayerAndRegisterIfNotExists(String userUuid) {
        // Determine current history-player and its id
        LizHistoryPlayer lizHistoryPlayer = lizHistoryPlayerRepository.findByUserUuid(userUuid);
        if (lizHistoryPlayer == null) { // Register player if it was not
            lizHistoryPlayer = lizHistoryPlayerRepository.save(new LizHistoryPlayer(userUuid));
        }
        return lizHistoryPlayer;
    }


}
