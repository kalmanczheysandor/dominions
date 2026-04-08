package hu.kalmancheysandor.applications.dominions.utils.filehandler;

public class FileAccessException extends TFileException {
    public FileAccessException(final String path) {
        super(path);
    }
}
