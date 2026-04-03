package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.DataDto;
import bekhruz.com.cinemora.dto.ResourceFileDto;
import bekhruz.com.cinemora.exception.FileStorageException;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.util.BaseUtils;
import bekhruz.com.cinemora.util.ImageUtils;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.springframework.http.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final BaseUtils baseUtils;
    private final ImageUtils imageUtils;

    @Value("${root.path}")
    private String rootPath;

    public DataDto<ResourceFileDto> storeFile(MultipartFile file, Integer minWidth, Integer minHeight) {
        Path rootLocation = getRootLocation();
        if (file.isEmpty())
            throw new FileStorageException("file.invalid.path");
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (originalFilename.contains(".."))
            throw new GenericNotFoundException("Failed to store file with relative path outside current directory");
        String contentType = Objects.requireNonNull(file.getContentType());
        String fileNamePrefix = Objects.requireNonNull(StringUtils.split(originalFilename, "."))[0];
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String newFileName = baseUtils.encodeToMd5(fileNamePrefix) + new Date().getTime() + "." + fileExtension;
        if (contentType.startsWith("image") && !contentType.contains("svg+xml")) {
            try {
                // minioService ga yuborish
                if (!baseUtils.isEmpty(fileExtension) && (!"png".equals(fileExtension))) {
                    if (minWidth != null && minHeight != null)
                        imageUtils.compressImage(rootLocation.resolve(newFileName).toString(),
                                rootLocation.resolve(newFileName).toString(), minWidth, minHeight);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        } else {
            try {
                // minioService ga yuborish
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return new DataDto<>();
    }

    private Path getRootLocation() {
        Path path = Paths.get(rootPath);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            log.error("Cannot create directory: {}", path, e);
            throw new FileStorageException("file.invalid.path", e);
        }

        return path;
    }
}
