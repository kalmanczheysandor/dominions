package hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.exception;

public class FileAlreadyExistsException extends TFileException {
    public FileAlreadyExistsException(final String path) {
        super(path);
    }
}
