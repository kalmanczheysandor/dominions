package hu.kalmancheysandor.applications.dominions.utils.filehandler;

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
