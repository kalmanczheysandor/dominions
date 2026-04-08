package hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.exception;

public class DirectoryAccessException extends TFileException {
    public DirectoryAccessException(final String path) {
        super(path);
    }
}
