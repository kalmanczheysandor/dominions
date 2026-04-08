package hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordInvalidFieldException;

public class GameScenarioInvalidGameMapContentException extends RecordInvalidFieldException {

    public GameScenarioInvalidGameMapContentException() {
    }

    @Override
    public String toString() {
        return "GameScenarioInvalidGameMapContentException{}";
    }
}
