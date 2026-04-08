package hu.kalmancheysandor.applications.dominions.utils.filehandler;

public class DirectoryAccessException extends TFileException {
    public DirectoryAccessException(final String path) {
        super(path);
    }
}
