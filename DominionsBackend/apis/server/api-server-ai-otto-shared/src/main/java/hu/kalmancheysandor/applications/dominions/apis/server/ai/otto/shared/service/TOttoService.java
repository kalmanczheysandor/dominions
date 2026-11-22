package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.service;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.service.TAiService;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.history.OttoHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.history.OttoHistoryScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.history.OttoHistorySession;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.history.OttoHistoryPlayerNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.history.OttoHistoryScenarioNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.history.OttoHistorySessionNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.history.OttoHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.history.OttoHistoryScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.history.OttoHistorySessionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TOttoService extends TAiService {
    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private OttoHistoryScenarioRepository ottoHistoryScenarioRepository;

    @Autowired
    private OttoHistoryPlayerRepository ottoHistoryPlayerRepository;

    @Autowired
    private OttoHistorySessionRepository ottoHistorySessionRepository;


    protected void registerHistoryScenarioIfNotExists(String scenarioUuid, String scenarioName) {
        // Determine current record and its id
        OttoHistoryScenario ottoHistoryScenario = ottoHistoryScenarioRepository.findByScenarioUuid(scenarioUuid);
        if (ottoHistoryScenario == null) { // Register it if it was not
            ottoHistoryScenarioRepository.save(new OttoHistoryScenario(scenarioUuid, scenarioName));
        }
    }


    protected void registerHistoryPlayerIfNotExists(String userUuid, String userName) {
        // Determine current record and its id
        OttoHistoryPlayer ottoHistoryPlayer = ottoHistoryPlayerRepository.findByUserUuid(userUuid);
        if (ottoHistoryPlayer == null) { // Register it if it was not
            ottoHistoryPlayerRepository.save(new OttoHistoryPlayer(userUuid, userName));
        }
    }

    protected void registerHistorySessionIfNotExists(String sessionUuid) {
        // Determine current record and its id
        OttoHistorySession ottoHistorySession = ottoHistorySessionRepository.findBySessionUuid(sessionUuid);
        if (ottoHistorySession == null) { // Register it if it was not
            ottoHistorySessionRepository.save(new OttoHistorySession(sessionUuid));
        }
    }

    protected OttoHistoryScenario accessHistoryScenario(String scenarioUuid) {

        OttoHistoryScenario ottoHistoryScenario = ottoHistoryScenarioRepository.findByScenarioUuid(scenarioUuid);
        if (ottoHistoryScenario == null) {
            throw new OttoHistoryScenarioNotFoundByUuidException(scenarioUuid);
        }
        return ottoHistoryScenario;
    }


    protected OttoHistoryPlayer accessHistoryPlayer(String userUuid) {
        OttoHistoryPlayer ottoHistoryPlayer = ottoHistoryPlayerRepository.findByUserUuid(userUuid);
        if (ottoHistoryPlayer == null) {
            throw new OttoHistoryPlayerNotFoundByUuidException(userUuid);
        }
        return ottoHistoryPlayer;
    }

    protected OttoHistorySession accessHistorySession(String sessionUuid) {
        OttoHistorySession ottoHistorySession = ottoHistorySessionRepository.findBySessionUuid(sessionUuid);
        if (ottoHistorySession == null) {
            throw new OttoHistorySessionNotFoundByUuidException(sessionUuid);
        }
        return ottoHistorySession;
    }
}
