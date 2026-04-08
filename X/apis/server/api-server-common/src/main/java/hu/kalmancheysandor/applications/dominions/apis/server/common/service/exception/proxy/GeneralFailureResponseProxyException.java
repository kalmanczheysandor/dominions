package hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.proxy;

import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;

public class GeneralFailureResponseProxyException extends TProxyException {
    private GeneralFailureResponse response;

    public GeneralFailureResponseProxyException(GeneralFailureResponse response) {
        this.response = response;
    }

    public GeneralFailureResponse getResponse() {
        return response;
    }
}
