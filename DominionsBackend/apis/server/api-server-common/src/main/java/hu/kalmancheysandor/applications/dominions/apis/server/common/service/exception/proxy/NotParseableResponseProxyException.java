package hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.proxy;

public class NotParseableResponseProxyException extends TProxyException {
    private String content;

    public NotParseableResponseProxyException(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
