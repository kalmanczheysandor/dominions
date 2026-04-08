package hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.proxy;

import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.error.GeneralErrorResponse;

public class GeneralErrorResponseProxyException extends TProxyException {
    private GeneralErrorResponse response;

    public GeneralErrorResponseProxyException(GeneralErrorResponse response) {
        this.response = response;
    }

    public GeneralErrorResponse getResponse() {
        return response;
    }
}
