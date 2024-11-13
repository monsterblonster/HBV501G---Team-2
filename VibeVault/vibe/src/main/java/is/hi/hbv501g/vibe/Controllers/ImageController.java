package is.hi.hbv501g.vibe.Controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final Path userImageLocation = Paths.get(System.getProperty("user.dir") + "/uploads/users");
    private final Path eventImageLocation = Paths.get(System.getProperty("user.dir") + "/uploads/events");

    @RequestMapping(value = "/users/{filename:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> serveUserImage(@PathVariable String filename) {
        try {
            Path filePath = userImageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/events/{filename:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> serveEventImage(@PathVariable String filename) {
        try {
            Path filePath = eventImageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

