package de.gregord.drlleaderboardbackend.controllers;

import org.imgscalr.Scalr;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/img")
public class ImgCacheController {

    private final Path storagePath = Paths.get("imgcache");
    private final RestTemplate restTemplate;
    // Only let 2 Threads download a image at the same time to save memory
    private final Semaphore semaphore = new Semaphore(2);

    public ImgCacheController() {
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        try {
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory at " + storagePath, e);
        }
    }

    @GetMapping("/{imgName}.png")
    public ResponseEntity<InputStreamResource> getImg(@RequestParam String url) {
        try {
            String fileName = url.replaceAll("[^a-zA-Z0-9.-]", "_") + ".png";
            Path imagePath = storagePath.resolve(fileName);

            if (!Files.exists(imagePath)) {
                semaphore.acquire();
                try {
                    // Download image
                    BufferedImage imgBuf = ImageIO.read(new ByteArrayInputStream(
                            restTemplate.getForObject(url, byte[].class)
                    ));

                    BufferedImage imgResized = Scalr.resize(
                            imgBuf,
                            Scalr.Method.BALANCED,
                            Scalr.Mode.FIT_TO_HEIGHT,
                            500,
                            500, Scalr.OP_ANTIALIAS
                    );

                    ImageIO.write(imgResized, "png", imagePath.toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            InputStreamResource resource = null;
            try {
                resource = new InputStreamResource(Files.newInputStream(imagePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", e);
        } finally {
            semaphore.release();
        }
    }
}
