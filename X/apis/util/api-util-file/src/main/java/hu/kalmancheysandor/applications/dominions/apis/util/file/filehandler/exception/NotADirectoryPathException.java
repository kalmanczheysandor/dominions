package hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.exception;

public class NotADirectoryPathException extends TFileException {
    public NotADirectoryPathException(final String path) {
        super(path);
    }
}
