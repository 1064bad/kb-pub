package app;

public interface Settings {
    String HOST = "localhost";
    int PORT = 5566;
    boolean DEBUG_ON = false;
    String SERVER_FILE_STORAGE_BASE_PATH = "/home/kb/test/file-storage/remote";
    String CLIENT_FILE_STORAGE_BASE_PATH = "/home/kb/test/file-storage/local";
    String DEFAULT_FILENAME_TO_UPLOAD = "/home/kb/test/file-storage/img.jpeg";
    String DEFAULT_FILENAME_TO_DOWNLOAD = "img.jpeg";
}
