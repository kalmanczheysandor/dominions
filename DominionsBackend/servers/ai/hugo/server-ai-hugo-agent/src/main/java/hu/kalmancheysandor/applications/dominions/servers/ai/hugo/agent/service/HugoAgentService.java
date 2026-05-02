package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.agent.service;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.HugoAiAdvancedEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.HugoFirstHeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.HugoSecondHeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.IHeuristicAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.IHeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.heuristic.HugoHeuristicEvaluatorType;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.training.HugoCharacter;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.training.HugoVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character.HugoCharacterAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character.HugoCharacterNotFoundByCodeException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character.HugoCharacterNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic.evaluator.HugoUnexpectedHeuristicEvaluatorTypeCodeException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant.HugoVariantAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant.HugoVariantNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.training.HugoCharacterRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.service.THugoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HugoAgentService extends THugoService {

    @Autowired
    private HugoCharacterRepository hugoCharacterRepository;

    public AiDecisionResponse generateResponse(AiDecisionRequest request) {

        //
        String sessionUuid = request.getSessionUuid();
        String scenarioUuid = request.getScenarioUuid();
        String scenarioName = request.getScenarioName();
        String userUuid = request.getYourUserUuid();
        String userName = request.getYourUserName();
        String myCharacterCode = request.getPlayerCharacterCode();

        // Test whether working-profile (=character) is existing and accessible/enabled
        assertCharacterExistsAndIsEnabledByCharacterCode(myCharacterCode);

        // Register local representations if not exist
        registerHistorySessionIfNotExists(sessionUuid);
        registerHistoryPlayerIfNotExists(userUuid, userName);
        registerHistoryScenarioIfNotExists(scenarioUuid, scenarioName);

        //
        IHeuristicAiEngine engine = new HugoAiAdvancedEngine();
        engine.registerHeuristicEvaluator(
                determineHeuristicEvaluatorByCharacterCode(myCharacterCode)
        );

        // Generate decision and response
        return convertDecisionResultToResponse(
                engine.makeDecision(
                        convertRequestToDecisionContext(request)
                )
        );
    }

    private void assertCharacterExistsAndIsEnabledByCharacterCode(String characterCode) {
        HugoCharacter character = hugoCharacterRepository.findByCode(characterCode);
        if (character == null) {
            throw new HugoCharacterNotFoundByCodeException(characterCode);
        }
        if (!character.isEnabled()) {
            throw new HugoCharacterAssociationRestrictedException(character.getId(), character.getName());
        }
    }

    private IHeuristicEvaluator determineHeuristicEvaluatorByCharacterCode(String characterCode) {
        HugoHeuristicEvaluatorType heuristicEvaluatorType = determineHeuristicEvaluatorTypeFromCharacterCode(characterCode);
        if (HugoHeuristicEvaluatorType.HUGO_FIRST_HEURISTIC_EVALUATOR.equals(heuristicEvaluatorType)) {
            return new HugoFirstHeuristicEvaluator();
        } else if (HugoHeuristicEvaluatorType.HUGO_SECOND_HEURISTIC_EVALUATOR.equals(heuristicEvaluatorType)) {
            return new HugoSecondHeuristicEvaluator();
        } else {
            throw new HugoUnexpectedHeuristicEvaluatorTypeCodeException(heuristicEvaluatorType.toString());
        }
    }

    private HugoHeuristicEvaluatorType determineHeuristicEvaluatorTypeFromCharacterCode(String code) {
        // Find and validate character
        HugoCharacter character = hugoCharacterRepository.findByCode(code);
        if (character == null) {
            throw new HugoCharacterNotFoundByUuidException(code);
        }
        if (!character.isEnabled()) {
            throw new HugoCharacterAssociationRestrictedException(character.getId(), character.getName());
        }

        // Find variant
        HugoVariant variant = character.getVariant();
        if (variant == null) {
            throw new HugoVariantNotFoundException();
        }
        if (!variant.isEnabled()) {
            throw new HugoVariantAssociationRestrictedException(character.getId(), character.getName());
        }

        //
        if (HugoHeuristicEvaluatorType.HUGO_FIRST_HEURISTIC_EVALUATOR.toString().equals(variant.getHeuristicCode())) {
            return HugoHeuristicEvaluatorType.HUGO_FIRST_HEURISTIC_EVALUATOR;
        } else if (HugoHeuristicEvaluatorType.HUGO_SECOND_HEURISTIC_EVALUATOR.toString().equals(variant.getHeuristicCode())) {
            return HugoHeuristicEvaluatorType.HUGO_SECOND_HEURISTIC_EVALUATOR;
        } else {
            throw new HugoUnexpectedHeuristicEvaluatorTypeCodeException(variant.getHeuristicCode());
        }
    }

}
