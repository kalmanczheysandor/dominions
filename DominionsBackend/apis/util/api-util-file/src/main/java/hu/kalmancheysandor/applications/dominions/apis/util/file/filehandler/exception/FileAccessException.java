package hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.exception;

public class FileAccessException extends TFileException {
    public FileAccessException(final String path) {
        super(path);
    }
}
