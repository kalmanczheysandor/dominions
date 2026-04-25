package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class HugoCharacterNotFoundByCodeException extends CustomException {
    private String code;
    public HugoCharacterNotFoundByCodeException(String code) {
        super("RecordNotFound");
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
