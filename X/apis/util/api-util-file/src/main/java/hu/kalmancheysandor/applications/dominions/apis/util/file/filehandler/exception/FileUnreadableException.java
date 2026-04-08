package hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.exception;

public class FileUnreadableException extends TFileException {
    public FileUnreadableException(final String path) {
        super(path);
    }
}
