package hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.exception;

public class FileDeleteException extends TFileException {
    public FileDeleteException(final String path) {
        super(path);
    }

    @Override
    public String toString() {
        return "FileDeleteException{" +
            "path='" + getPath() + '\'' +
            '}';
    }
}
