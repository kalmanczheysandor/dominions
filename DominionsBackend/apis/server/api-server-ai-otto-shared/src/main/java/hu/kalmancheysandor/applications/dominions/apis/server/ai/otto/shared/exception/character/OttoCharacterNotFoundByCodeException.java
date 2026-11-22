package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class OttoCharacterNotFoundByCodeException extends CustomException {
    private String code;

    public OttoCharacterNotFoundByCodeException(String code) {
        super("RecordNotFound");
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
