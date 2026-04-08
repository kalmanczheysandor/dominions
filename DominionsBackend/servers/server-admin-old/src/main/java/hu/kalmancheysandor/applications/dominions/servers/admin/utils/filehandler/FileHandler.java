package hu.kalmancheysandor.applications.dominions.utils.filehandler;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {


    public Result findFile(@NotBlank String fullPath) {
        try {
            Path fullPathObj = Paths.get(fullPath).normalize();
            Resource resource = new UrlResource(fullPathObj.toUri());

            // Checking
            if (!resource.exists()) {
                throw new FileAccessException(fullPath);
            }
            if (!resource.isReadable()) {
                throw new FileUnreadableException(fullPath);
            }

            String etag = String.valueOf(resource.hashCode());
            long lastModified = Files.getLastModifiedTime(fullPathObj).toMillis();

            return new Result(resource, etag, lastModified);
        } catch (MalformedURLException e) {
            throw new GeneralFileException();
        } catch (IOException e) {
            throw new GeneralFileException();
        }
    }


    public void saveBase64Image(@NotBlank String fullPath, @NotBlank String base64Image) {
        try {

            Path fullPathObj = Paths.get(fullPath);
            Path folderPathObj = fullPathObj.getParent();

            // Checking: whether the file already exists
            if (Files.exists(fullPathObj)) {
                throw new FileAlreadyExistsException(fullPath);
            }

            // Check whether it is a directory path
            if (Files.exists(folderPathObj)) {
                if (!Files.isDirectory(folderPathObj)) {
                    throw new NotADirectoryPathException(folderPathObj.toString());
                }
            }

            // Attempt to create the missing directory(s)
            try {
                Files.createDirectories(fullPathObj.getParent());
            } catch (IOException e) {
                throw new DirectoryCreationException(fullPathObj.getParent().toString());
            }

            // Execution
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            try (FileOutputStream fos = new FileOutputStream(fullPath)) {
                fos.write(imageBytes);
            }
        } catch (IOException exp) {
            throw new GeneralFileException(fullPath);
        }
    }

    public void updateBase64Image(@NotBlank String fullPath, @NotBlank String base64Image) {
        try {

            Path fullPathObj = Paths.get(fullPath);

            // Delete if exists
            if (Files.exists(fullPathObj)) {
                Files.delete(fullPathObj);
            }

            if(!base64Image.isEmpty()) {
                this.saveBase64Image(fullPath, base64Image);
            }
        } catch (IOException exp) {
            throw new GeneralFileException(fullPath);
        }
    }


    public void saveMultipartFile(@NotBlank String folderPath, @NotNull MultipartFile file, @NotBlank String newFilename) {
        try {
            Path folderPathObj = Paths.get(folderPath);

            // Attempt to create the missing directory(s) if not exists
            if (!Files.exists(folderPathObj)) {
                try {
                    Files.createDirectories(folderPathObj);
                } catch (IOException e) {
                    throw new DirectoryCreationException(folderPathObj.getParent().toString());
                }
            }

            // Check whether it is a directory path
            if (!Files.isDirectory(folderPathObj)) {
                throw new NotADirectoryPathException(folderPathObj.toString());
            }

            // Initialise file path object
            Path filePathObj = folderPathObj.resolve(newFilename).normalize();

            // Checking: whether the file already exists
            if (Files.exists(filePathObj)) {
                throw new FileAlreadyExistsException(folderPath);
            }

            // Save file
            try {
                file.transferTo(new File(filePathObj.toString()));
            } catch (IOException e) {
                throw new DirectoryCreationException(filePathObj.toString());
            }

        } catch (TFileException e) {
            throw e;
        }
    }

    public void saveMultipartFile(@NotBlank String folderPath, @NotNull MultipartFile file) {
        String fileName = file.getOriginalFilename();
        this.saveMultipartFile(folderPath, file, fileName);
    }


    public void updateMultipartFile(@NotBlank String folderPath, @NotNull MultipartFile file, @NotBlank String newFilename) {
        try {
            Path fullPathObj = Paths.get(folderPath + "/" + newFilename);

            // Attempt to delete file if exists
            if (Files.exists(fullPathObj)) {
                try {
                    Files.delete(fullPathObj);
                } catch (IOException exp) {
                    throw new FileDeleteException(fullPathObj.toString());
                }
            }

            // Save as new file
            this.saveMultipartFile(folderPath, file, newFilename);
        } catch (TFileException e) {
            throw e;
        }
    }


    public void updateMultipartFile(@NotBlank String folderPath, @NotNull MultipartFile file) {
        String fileName = file.getOriginalFilename();
        this.updateMultipartFile(folderPath, file, fileName);
    }

    public void deleteFileIfExists(@NotBlank String fullPath) {
        try {

            Path fullPathObj = Paths.get(fullPath);

            // If not exists than silent return
            if (!Files.exists(fullPathObj)) {
                return;
            }

            // Check whether it is a directory path
            if (!Files.isRegularFile(fullPathObj)) {
                throw new NotAFilePathException(fullPathObj.toString());
            }

            // Delete if exists
            Files.delete(fullPathObj);
        } catch (IOException exp) {
            throw new GeneralFileException(fullPath);
        }
    }

    public void deleteDirectoryRecursively(@NotBlank String fullPath) {
        try {

            Path fullPathObj = Paths.get(fullPath);

            // If not exists than silent return
            if (!Files.exists(fullPathObj)) {
                return;
            }

            // Check whether it is a directory path
            if (!Files.isDirectory(fullPathObj)) {
                throw new NotADirectoryPathException(fullPathObj.toString());
            }

            // Delete
            try (var paths = Files.walk(fullPathObj)) {
                // Sort paths in reverse order to delete files and directories from the deepest level
                paths.sorted((path1, path2) -> path2.compareTo(path1)) // Delete the deepest files first
                    .forEach(path -> {
                        try {
                            Files.delete(path); // Delete each file and directory
                        } catch (IOException e) {
                            throw new GeneralFileException(path.toString());
                        }
                    });
            } catch (IOException e) {
                throw new GeneralFileException(fullPathObj.toString());
            }
        } catch (TFileException e) {
            throw e;
        }
    }

    public List<String> listFilesOfFolder(@NotBlank String folderPath) {
        return this.listFilesOfFolder(folderPath, false);
    }

    public List<String> listFilesOfFolder(@NotBlank String folderPath, boolean isSilentMode) {
        Path folderPathObj = Paths.get(folderPath);

        // Attempt to create the missing directory(s) if not exists
        if (!Files.exists(folderPathObj)) {
            if (isSilentMode) {
                return new ArrayList<>();
            }
            throw new DirectoryAccessException(folderPathObj.toString());
        }

        // Check whether it is a directory path
        if (!Files.isDirectory(folderPathObj)) {
            throw new NotADirectoryPathException(folderPathObj.toString());
        }

        try {
            return Files.list(folderPathObj)
                .filter(Files::isRegularFile)  // Csak fájlok
                .map(path -> path.getFileName().toString())  // Kinyerjük a fájl nevét
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new GeneralFileException(folderPathObj.toString());
        }
    }

    public static class Result {
        private Resource resource;
        private String etag;

        private long lastModified;

        private Result(Resource resource, String etag, long lastModified) {
            this.resource = resource;
            this.etag = etag;
            this.lastModified = lastModified;
        }

        public Resource getResource() {
            return resource;
        }

        public String getEtag() {
            return etag;
        }

        public long getLastModified() {
            return lastModified;
        }

    }

}
