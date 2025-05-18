package app.server.filestorage;

import app.Settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class FileSystemService {
    private final Pattern usernameRegex = Pattern.compile("^\\w+$");

    public boolean isUsernameValid(String username) {
        return usernameRegex.matcher(username).matches();
    }

    public List<String> listUserFiles(String username) {
        var userFilePath = getUserDirectory(username);
        if (Files.notExists(userFilePath)) {
            return List.of();
        }
        try (var files = Files.list(userFilePath)) {
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

    public void delete(String username, String filename) {
        try {
            Files.delete(getUserFilePath(username, filename));
        } catch (IOException e) {
            throw new FileStorageException(e);
        }
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

    private Path getUserDirectory(String username) {
        return Path.of(Settings.SERVER_FILE_STORAGE_BASE_PATH, username);
    }
    private Path getUserFilePath(String username, String filename) {
        return Path.of(getUserDirectory(username).toString(), filename);
    }
}
