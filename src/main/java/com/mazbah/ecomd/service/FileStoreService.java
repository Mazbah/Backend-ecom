package com.mazbah.ecomd.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStoreService {
    public String store(MultipartFile file);
    public Stream<Path> loadAll();
    public Resource load(String fileName);
}
