package is.hi.hbv501g.vibe.Services.Implementation;

import is.hi.hbv501g.vibe.Services.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImplementation implements FileStorageService {

    // Define an absolute path for the uploads directory
    private final Path fileStorageLocation = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/uploads");

    private static final int IMAGE_WIDTH = 200;
    private static final int IMAGE_HEIGHT = 200;

    @Override
    public String storeFile(MultipartFile file, String filename) {
        try {
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Path targetLocation = fileStorageLocation.resolve(filename);
            
            File targetFile = targetLocation.toFile();
            if (targetFile.exists() && !targetFile.canWrite()) {
                throw new IOException("Cannot write to target file: " + targetLocation);
            }

            // Use Thumbnails to resize and save the image
            Thumbnails.of(file.getInputStream())
                    .size(IMAGE_WIDTH, IMAGE_HEIGHT)
                    .toFile(targetFile);

            return "/uploads/" + filename;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }

    @Override
    public String getFilePath(String filename) {
        return fileStorageLocation.resolve(filename).toString();
    }
}

