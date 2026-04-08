package hu.kalmancheysandor.applications.dominions.utils.filehandler;

public class DirectoryCreationException extends TFileException {
    public DirectoryCreationException(final String path) {
        super(path);
    }
}
