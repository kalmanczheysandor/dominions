package hu.kalmancheysandor.applications.dominions.servers.ai.otto.agent.service;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.*;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluator.OttoR1OperatorScoreEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluator.OttoR3OperatorScoreEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluator.OttoR2OperatorScoreEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoOperatorScoreEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.evaluator.OttoOperatorScoreEvaluatorType;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.training.OttoCharacter;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.training.OttoVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character.OttoCharacterAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character.OttoCharacterNotFoundByCodeException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character.OttoCharacterNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator.evaluator.OttoUnexpectedOperatorScoreEvaluatorTypeCodeException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant.OttoVariantAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant.OttoVariantNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.training.OttoCharacterRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.service.TOttoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OttoAgentService extends TOttoService {

    @Autowired
    private OttoCharacterRepository ottoCharacterRepository;

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
        IOttoEngine engine = new OttoEngine();
        engine.registerOperatorScoreEvaluator(
                determineCellScoreEvaluatorByCharacterCode(myCharacterCode)
        );

        // Generate decision and response
        return convertDecisionResultToResponse(
                engine.makeDecision(
                        convertRequestToDecisionContext(request)
                )
        );
    }

    private void assertCharacterExistsAndIsEnabledByCharacterCode(String characterCode) {
        OttoCharacter character = ottoCharacterRepository.findByCode(characterCode);
        if (character == null) {
            throw new OttoCharacterNotFoundByCodeException(characterCode);
        }
        if (!character.isEnabled()) {
            throw new OttoCharacterAssociationRestrictedException(character.getId(), character.getName());
        }
    }

    private IOttoOperatorScoreEvaluator determineCellScoreEvaluatorByCharacterCode(String characterCode) {
        OttoOperatorScoreEvaluatorType evaluatorType = determineOperatorScoreEvaluatorTypeFromCharacterCode(characterCode);

        if (OttoOperatorScoreEvaluatorType.R1.equals(evaluatorType)) {
            return new OttoR1OperatorScoreEvaluator();
        } else if (OttoOperatorScoreEvaluatorType.R2.equals(evaluatorType)) {
            return new OttoR2OperatorScoreEvaluator();
        } else if (OttoOperatorScoreEvaluatorType.R3.equals(evaluatorType)) {
            return new OttoR3OperatorScoreEvaluator();
        }
        else {
            throw new OttoUnexpectedOperatorScoreEvaluatorTypeCodeException(evaluatorType.toString());
        }
    }
    private OttoOperatorScoreEvaluatorType determineOperatorScoreEvaluatorTypeFromCharacterCode(String code) {
        // Find and validate character
        OttoCharacter character = ottoCharacterRepository.findByCode(code);
        if (character == null) {
            throw new OttoCharacterNotFoundByUuidException(code);
        }
        if (!character.isEnabled()) {
            throw new OttoCharacterAssociationRestrictedException(character.getId(), character.getName());
        }

        // Find variant
        OttoVariant variant = character.getVariant();
        if (variant == null) {
            throw new OttoVariantNotFoundException();
        }
        if (!variant.isEnabled()) {
            throw new OttoVariantAssociationRestrictedException(character.getId(), character.getName());
        }

        //
        if (OttoOperatorScoreEvaluatorType.R1.toString().equals(variant.getEvaluatorCode())) {
            return OttoOperatorScoreEvaluatorType.R1;
        } else if (OttoOperatorScoreEvaluatorType.R2.toString().equals(variant.getEvaluatorCode())) {
            return OttoOperatorScoreEvaluatorType.R2;
        } else if (OttoOperatorScoreEvaluatorType.R3.toString().equals(variant.getEvaluatorCode())) {
            return OttoOperatorScoreEvaluatorType.R3;
        }
        else {
            throw new OttoUnexpectedOperatorScoreEvaluatorTypeCodeException(variant.getEvaluatorCode());
        }
    }

}
