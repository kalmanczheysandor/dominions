package hu.kalmancheysandor.applications.dominions.utils.filehandler;

public class NotAFilePathException extends TFileException {
    public NotAFilePathException(final String path) {
        super(path);
    }
}
