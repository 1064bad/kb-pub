package app.server.filestorage;

import app.Settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class FileStorageService {
    private final Pattern usernameRegex = Pattern.compile("^\\w+$");

    public boolean isUsernameValid(String username) {
        return usernameRegex.matcher(username).matches();
    }

    public List<String> listUserFiles(String username) {
        var userFilePath = getUserDirectory(username);
        return listFilesFromDirectory(userFilePath);
    }

    public List<String> listUserFilesFromDirectory(String username, String directory) {
        var userFilePath = Path.of(getUserDirectory(username).toString(), directory);
        return listFilesFromDirectory(userFilePath);
    }

    private List<String> listFilesFromDirectory(Path filePath) {
        if (Files.notExists(filePath)) {
            return List.of();
        }
        try (var files = Files.list(filePath)) {
            return files
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .toList();
        } catch (Exception e) {
            throw new FileStorageException(e);
        }
    }

    public boolean fileExists(String username, String filename) {
        return Files.exists(getUserFilePath(username, filename));
    }

    public long size(String username, String filename) {
        try {
            return Files.size(getUserFilePath(username, filename));
        } catch (IOException e) {
            throw new FileStorageException(e);
        }
    }

    public InputStream getFileInputStream(String username, String filename) {
        var path = getUserFilePath(username, filename);
        if (Files.exists(path) && !Files.isRegularFile(path)) {
            throw new FileStorageException(STR."file '\{filename}' is not a regular file");
        }
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new FileStorageException(e);
        }
    }

    public OutputStream getFileOutputStream(String username, String filename) {
        var path = getUserFilePath(username, filename);
        if (Files.exists(path) && !Files.isRegularFile(path)) {
            throw new FileStorageException(STR."file '\{filename}' is not a regular file");
        }
        try {
            return Files.newOutputStream(path);
        } catch (IOException e) {
            throw new FileStorageException(e);
        }
    }

    public void deleteFile(String username, String filename) {
        var path = getUserFilePath(username, filename);
        if (Files.exists(path) && !Files.isRegularFile(path)) {
            throw new FileStorageException(STR."file '\{filename}' is not a regular file");
        }
        try {
            Files.delete(getUserFilePath(username, filename));
        } catch (IOException e) {
            throw new FileStorageException(e);
        }
    }

    public void createDirectory(String username, String directoryName) {
        var path = getUserFilePath(username, directoryName);
        if (fileExists(username, directoryName)) {
            throw new FileStorageException(STR."directory \{directoryName} already exists");
        }

        try {
            Files.createDirectory(path);
        } catch (Exception e) {
            throw new FileStorageException(e);
        }
    }

    public void moveFile(String username, String filePath, String destinationDirectory) {
        var fileName = Path.of(filePath).getFileName();
        var newPath = Path.of(getUserFilePath(username, destinationDirectory).toString(), fileName.toString());
        try {
            Files.move(getUserFilePath(username, filePath), newPath);
        } catch (Exception e) {
            throw new FileStorageException(e);
        }
    }

    private Path getUserDirectory(String username) {
        return Path.of(Settings.SERVER_FILE_STORAGE_BASE_PATH, username);
    }

    private Path getUserFilePath(String username, String filename) {
        return Path.of(getUserDirectory(username).toString(), filename);
    }
}
