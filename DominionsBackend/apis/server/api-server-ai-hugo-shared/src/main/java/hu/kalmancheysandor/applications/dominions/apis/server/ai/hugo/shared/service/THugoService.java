package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.service;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.service.TAiService;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.heuristic.HugoHeuristicEvaluatorType;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.history.HugoHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.history.HugoHistoryScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.history.HugoHistorySession;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.training.HugoCharacter;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.training.HugoVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character.HugoCharacterNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.history.HugoHistoryPlayerNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.history.HugoHistoryScenarioNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.history.HugoHistorySessionNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.history.HugoHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.history.HugoHistoryScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.history.HugoHistorySessionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class THugoService extends TAiService {
    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private HugoHistoryScenarioRepository hugoHistoryScenarioRepository;

    @Autowired
    private HugoHistoryPlayerRepository hugoHistoryPlayerRepository;


    @Autowired
    private HugoHistorySessionRepository hugoHistorySessionRepository;






    protected void registerHistoryScenarioIfNotExists(String scenarioUuid, String scenarioName) {
        // Determine current record and its id
        HugoHistoryScenario hugoHistoryScenario = hugoHistoryScenarioRepository.findByScenarioUuid(scenarioUuid);
        if (hugoHistoryScenario == null) { // Register it if it was not
            hugoHistoryScenarioRepository.save(new HugoHistoryScenario(scenarioUuid, scenarioName));
        }
    }


    protected void registerHistoryPlayerIfNotExists(String userUuid,String userName) {
        // Determine current record and its id
        HugoHistoryPlayer hugoHistoryPlayer = hugoHistoryPlayerRepository.findByUserUuid(userUuid);
        if (hugoHistoryPlayer == null) { // Register it if it was not
            hugoHistoryPlayerRepository.save(new HugoHistoryPlayer(userUuid, userName));
        }
    }

    protected void registerHistorySessionIfNotExists(String sessionUuid) {
        // Determine current record and its id
        HugoHistorySession hugoHistorySession = hugoHistorySessionRepository.findBySessionUuid(sessionUuid);
        if (hugoHistorySession == null) { // Register it if it was not
            hugoHistorySessionRepository.save(new HugoHistorySession(sessionUuid));
        }
    }

    protected HugoHistoryScenario accessHistoryScenario(String scenarioUuid) {

        HugoHistoryScenario hugoHistoryScenario = hugoHistoryScenarioRepository.findByScenarioUuid(scenarioUuid);
        if (hugoHistoryScenario == null) {
            throw new HugoHistoryScenarioNotFoundByUuidException(scenarioUuid);
        }
        return hugoHistoryScenario;
    }


    protected HugoHistoryPlayer accessHistoryPlayer(String userUuid) {
        HugoHistoryPlayer hugoHistoryPlayer = hugoHistoryPlayerRepository.findByUserUuid(userUuid);
        if (hugoHistoryPlayer == null) {
            throw new HugoHistoryPlayerNotFoundByUuidException(userUuid);
        }
        return hugoHistoryPlayer;
    }

    protected HugoHistorySession accessHistorySession(String sessionUuid) {
        HugoHistorySession hugoHistorySession = hugoHistorySessionRepository.findBySessionUuid(sessionUuid);
        if (hugoHistorySession == null) {
            throw new HugoHistorySessionNotFoundByUuidException(sessionUuid);
        }
        return hugoHistorySession;
    }



}
