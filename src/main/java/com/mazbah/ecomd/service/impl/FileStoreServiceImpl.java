package com.mazbah.ecomd.service.impl;

import com.mazbah.ecomd.config.StorageProperties;
import com.mazbah.ecomd.exceptions.StorageException;
import com.mazbah.ecomd.service.FileStoreService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStoreServiceImpl implements FileStoreService {

    private StorageProperties properties = new StorageProperties();
    Path rootLocation = Paths.get(properties.getLocation());

    public String store(MultipartFile file){
        try{
            if(file.isEmpty()){
                throw new StorageException("Failed to store empty file.");
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String uploadFileName = UUID.randomUUID().toString()+"."+extension;

            Path destinationFile = rootLocation.resolve(Paths.get(uploadFileName)).normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

                final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

                return baseUrl+"/fileUpload/files/"+uploadFileName;
            }
        }catch (IOException e){
            throw new StorageException("Failed to store file.", e);
        }
    }

    public Stream<Path> loadAll(){
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }catch (IOException e){
            throw new StorageException("Failed to read stored files", e);
        }
    }

    public Resource load(String fileName){
        try {
            Path file = rootLocation.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() || resource.isReadable()){
                return resource;
            } else {
                throw new RuntimeException("Couldn't read this file!!!");
            }
        }catch (MalformedURLException e){
            throw new RuntimeException("Error: "+e.getMessage());
        }
    }
}
