package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class LizCharacterNotFoundByCodeException extends CustomException {
    private String code;
    public LizCharacterNotFoundByCodeException(String code) {
        super("RecordNotFound");
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
