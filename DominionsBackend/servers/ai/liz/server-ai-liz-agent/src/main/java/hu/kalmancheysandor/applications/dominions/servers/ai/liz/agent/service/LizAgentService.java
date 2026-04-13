package hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent.service;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;

import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.LizNeuralConceptSnapshotChartDataItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralExecution;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralExecutionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralTrainingResultRepository;
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
    private LizNeuralExecutionRepository lizNeuralExecutionRepository;

    @Autowired
    private LizNeuralTrainingResultRepository lizNeuralTrainingResultRepository;


    public AiDecisionResponse generateResponse(AiDecisionRequest request) {
        int scenarioId = request.getScenarioId();
        int conceptId = request.getScenarioId();

        // Initialise the decision-making engine
        INeuralAiEngine aiDecisionEngine = new LizAiEngine();

        // Register all enemy network
        for (Map.Entry<Integer, AiDecisionRequest.Player> playerEntry : request.getPlayers().entrySet()) {
            AiDecisionRequest.Player player = playerEntry.getValue();

            INeuralNetwork neuralNetwork = generateNetwork(conceptId, scenarioId, player.getUserUuid());
            aiDecisionEngine.registerEnemyNetwork(player.getUserUuid(), neuralNetwork);
        }

        // Make a decision
        AiDecisionContext decisionContext = convertRequestToDecisionContext(request);
        AiDecisionResult result = aiDecisionEngine.makeDecision(decisionContext);

        // Generate response
        return convertDecisionResultToResponse(result);
    }

    private INeuralNetwork generateNetwork(int conceptId, int scenarioId, String userUuid) {
        //
        LizHistoryPlayer lizHistoryPlayer = findPlayerAndRegisterIfNotExists(userUuid);
        int historyPlayerId = lizHistoryPlayer.getId();

        // Find latest finished execution
        int latestExecutionId = 0;  // this is a fictive execution in order to provide network file creation for those players for whom no network is created
        LizNeuralExecution latestExecution = lizNeuralExecutionRepository.findLatestFinished(conceptId);
        if (latestExecution == null) {    // When there is no finished execution found
            latestExecutionId = latestExecution.getId();
        }

        //
        String directoryToStore = generateNetworkFileDirectoryString(conceptId, scenarioId, historyPlayerId);
        FileHandler.createDirectoryIfNotExist(directoryToStore);

        // Generate network filepath
        String fileName = generateNetworkFileName(conceptId, scenarioId, historyPlayerId, latestExecutionId);
        String filePath = directoryToStore + "/" + fileName;

        System.out.println("Network Generated: path:" + filePath);
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
