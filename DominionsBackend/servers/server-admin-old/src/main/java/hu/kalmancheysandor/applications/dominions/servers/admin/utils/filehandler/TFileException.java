package hu.kalmancheysandor.applications.dominions.utils.filehandler;

public abstract class TFileException extends RuntimeException {
    private String path;
    public TFileException(final String path) {
        this.path = path;
    }

    public TFileException() {
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "TFileException{" +
            "path='" + path + '\'' +
            '}';
    }
}
