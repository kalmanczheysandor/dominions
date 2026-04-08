package hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.exception;

public class NotAFilePathException extends TFileException {
    public NotAFilePathException(final String path) {
        super(path);
    }
}
