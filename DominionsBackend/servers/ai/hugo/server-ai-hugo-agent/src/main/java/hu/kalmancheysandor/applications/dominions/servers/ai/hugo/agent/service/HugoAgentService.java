package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.agent.service;


import hu.kalmancheysandor.applications.dominions.apis.ai.hugo.HugoAiAdvancedEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.hugo.HugoFirstHeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.IHeuristicAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.service.THugoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HugoAgentService extends THugoService {


    @Autowired
    private ModelMapper modelMapper;

    public AiDecisionResponse generateResponse(AiDecisionRequest request) {

        //
        String sessionUuid = request.getSessionUuid();
        String scenarioUuid = request.getScenarioUuid();
        String scenarioName = request.getScenarioName();
        String userUuid = request.getYourUserUuid();
        String userName = request.getYourUserName();
        String myCharacterCode = request.getPlayerCharacterCode();

        // Register local representations if not exist
        registerHistorySessionIfNotExists(sessionUuid);
        registerHistoryPlayerIfNotExists(userUuid,userName);
        registerHistoryScenarioIfNotExists(scenarioUuid, scenarioName);

        //
        IHeuristicAiEngine engine = new HugoAiAdvancedEngine();
        engine.registerHeuristicEvaluator(new HugoFirstHeuristicEvaluator());


        // Generate decision and response
        return convertDecisionResultToResponse(
                engine.makeDecision(
                        convertRequestToDecisionContext(request)
                )
        );
    }


}
