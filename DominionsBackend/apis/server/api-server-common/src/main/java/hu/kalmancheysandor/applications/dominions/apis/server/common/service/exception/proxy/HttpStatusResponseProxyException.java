package hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.proxy;

public class HttpStatusResponseProxyException extends TProxyException {
    private int statusCode;

    public HttpStatusResponseProxyException(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
