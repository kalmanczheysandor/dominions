package hu.kalmancheysandor.applications.dominions.utils.filehandler;

public class FileUnreadableException extends TFileException {
    public FileUnreadableException(final String path) {
        super(path);
    }
}
