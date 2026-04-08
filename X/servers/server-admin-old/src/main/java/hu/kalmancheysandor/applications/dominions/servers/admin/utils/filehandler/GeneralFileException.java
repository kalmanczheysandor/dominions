package hu.kalmancheysandor.applications.dominions.utils.filehandler;

public class GeneralFileException extends TFileException {
    public GeneralFileException(final String path) {
        super(path);
    }

    public GeneralFileException() {
    }
}
