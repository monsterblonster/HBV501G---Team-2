package is.hi.hbv501g.vibe.Services.Implementation;

import is.hi.hbv501g.vibe.Services.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.io.File;


@Service
public class FileStorageServiceImplementation implements FileStorageService {

    // separate directories for user and event images
    private final Path userImageLocation = Paths.get(System.getProperty("user.dir") + "/uploads/users");
    private final Path eventImageLocation = Paths.get(System.getProperty("user.dir") + "/uploads/events");
    private final Path groupImageLocation = Paths.get(System.getProperty("user.dir") + "/uploads/groups");

    private static final int IMAGE_WIDTH = 200;
    private static final int IMAGE_HEIGHT = 200;

    @Override
    public String storeFile(MultipartFile file, String filename, String type) {
        Path storageLocation = switch (type) {
            case "event" -> eventImageLocation;
            case "group" -> groupImageLocation;
            default -> userImageLocation;
        };

        try {
            if (!Files.exists(storageLocation)) {
                Files.createDirectories(storageLocation);
            }

            Path targetLocation = storageLocation.resolve(filename);

            File targetFile = targetLocation.toFile();
            if (targetFile.exists() && !targetFile.canWrite()) {
                throw new IOException("Cannot write to target file: " + targetLocation);
            }

            Thumbnails.of(file.getInputStream())
                    .size(IMAGE_WIDTH, IMAGE_HEIGHT)
                    .toFile(targetFile);

            return "/images/" + type + "s/" + filename;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }



    @Override
    public String getFilePath(String filename) {
        return userImageLocation.resolve(filename).toString();
    }
}

