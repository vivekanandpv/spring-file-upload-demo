package com.example.springfileuploaddemo;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("api/v1/sample")
public class SampleController {

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getSample(@PathVariable String fileName) {
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream("uploads/" + fileName));
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (FileNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        try {
            byte[] fileBytes = file.getBytes();
            Path filePath = Paths.get("uploads", file.getOriginalFilename());
            Files.write(filePath, fileBytes);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException ex) {
            System.out.println("Cannot write: " + ex.getMessage());
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
