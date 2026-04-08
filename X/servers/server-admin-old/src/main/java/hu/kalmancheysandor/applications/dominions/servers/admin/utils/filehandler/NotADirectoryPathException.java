package hu.kalmancheysandor.applications.dominions.utils.filehandler;

public class NotADirectoryPathException extends TFileException {
    public NotADirectoryPathException(final String path) {
        super(path);
    }
}
