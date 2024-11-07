package is.hi.hbv501g.vibe.Services.Implementation;

import is.hi.hbv501g.vibe.Services.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImplementation implements FileStorageService {

    private final Path fileStorageLocation = Paths.get("VibeVault/vibe/src/main/resources/static/uploads");

    private static final int IMAGE_WIDTH = 200;
    private static final int IMAGE_HEIGHT = 200;

    @Override
    public String storeFile(MultipartFile file, String filename) {
        try {
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Path targetLocation = fileStorageLocation.resolve(filename);

            Thumbnails.of(file.getInputStream())
                    .size(IMAGE_WIDTH, IMAGE_HEIGHT)
                    .toFile(targetLocation.toFile());

            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }

    @Override
    public String getFilePath(String filename) {
        return fileStorageLocation.resolve(filename).toString();
    }
}
