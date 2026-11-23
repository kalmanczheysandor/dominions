package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.agent.service;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.HugoEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalutor.HugoH0HeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalutor.HugoH1HeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalutor.HugoH2HeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalutor.HugoH3HeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoHeuristicEvaluator;
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
        IHugoEngine engine = new HugoEngine();
        engine.setSearchDepthLimit(
                determineSearchDepthLimit(myCharacterCode)
        );
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

    private IHugoHeuristicEvaluator determineHeuristicEvaluatorByCharacterCode(String characterCode) {
        HugoHeuristicEvaluatorType heuristicEvaluatorType = determineHeuristicEvaluatorTypeFromCharacterCode(characterCode);

        if (HugoHeuristicEvaluatorType.H0.equals(heuristicEvaluatorType)) {
            return new HugoH0HeuristicEvaluator();
        } else if (HugoHeuristicEvaluatorType.H1.equals(heuristicEvaluatorType)) {
            return new HugoH1HeuristicEvaluator();
        } else if (HugoHeuristicEvaluatorType.H2.equals(heuristicEvaluatorType)) {
            return new HugoH2HeuristicEvaluator();
        } else if (HugoHeuristicEvaluatorType.H3.equals(heuristicEvaluatorType)) {
            return new HugoH3HeuristicEvaluator();
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
        if (HugoHeuristicEvaluatorType.H0.toString().equals(variant.getHeuristicCode())) {
            return HugoHeuristicEvaluatorType.H0;
        } else if (HugoHeuristicEvaluatorType.H1.toString().equals(variant.getHeuristicCode())) {
            return HugoHeuristicEvaluatorType.H1;
        } else if (HugoHeuristicEvaluatorType.H2.toString().equals(variant.getHeuristicCode())) {
            return HugoHeuristicEvaluatorType.H2;
        } else if (HugoHeuristicEvaluatorType.H3.toString().equals(variant.getHeuristicCode())) {
            return HugoHeuristicEvaluatorType.H3;
        } else {
            throw new HugoUnexpectedHeuristicEvaluatorTypeCodeException(variant.getHeuristicCode());
        }
    }

    private int determineSearchDepthLimit(String code) {
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


        return variant.getConfSearchDepth();
    }

}
