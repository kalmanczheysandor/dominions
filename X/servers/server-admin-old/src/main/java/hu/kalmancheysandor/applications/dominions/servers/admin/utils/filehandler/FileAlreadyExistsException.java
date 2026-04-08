package hu.kalmancheysandor.applications.dominions.utils.filehandler;

public class FileAlreadyExistsException extends TFileException {
    public FileAlreadyExistsException(final String path) {
        super(path);
    }
}
