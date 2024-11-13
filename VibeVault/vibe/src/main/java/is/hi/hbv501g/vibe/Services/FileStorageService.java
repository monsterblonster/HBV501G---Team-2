package is.hi.hbv501g.vibe.Services;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file, String filename, boolean isEvent);
    String getFilePath(String filename);
}
