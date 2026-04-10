package bekhruz.com.cinemora.service;

import bekhruz.com.cinemora.dto.response.DataDto;
import bekhruz.com.cinemora.dto.file.ResourceFileDto;
import bekhruz.com.cinemora.exception.FileStorageException;
import bekhruz.com.cinemora.exception.GenericNotFoundException;
import bekhruz.com.cinemora.util.BaseUtils;
import bekhruz.com.cinemora.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final MinioService minioService;

    @Value("${root.path}")
    private String rootPath;

    public DataDto<ResourceFileDto> storeFile(MultipartFile file, Integer minWidth, Integer minHeight) {
        Path rootLocation = getRootLocation();
        if (file.isEmpty())
            throw new FileStorageException("file.invalid.path");

        String originalFilename = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename())
        );
        if (originalFilename.contains(".."))
            throw new GenericNotFoundException("Failed to store file with relative path");

        String contentType = Objects.requireNonNull(file.getContentType());
        String fileNamePrefix = Objects.requireNonNull(StringUtils.split(originalFilename, "."))[0];
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String newFileName = baseUtils.encodeToMd5(fileNamePrefix) + new Date().getTime() + "." + fileExtension;
        String path;
        if (contentType.startsWith("image") && !contentType.contains("svg+xml")) {
            try {
                MultipartFile fileToUpload = file;
                if (!baseUtils.isEmpty(fileExtension) && (!"png".equals(fileExtension))) {
                    if (minWidth != null && minHeight != null)
                        imageUtils.compressImage(rootLocation.resolve(newFileName).toString(),
                                rootLocation.resolve(newFileName).toString(), minWidth, minHeight);
                }
                path = minioService.saveFile(fileToUpload, originalFilename);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        } else {
            path = minioService.saveFile(file, originalFilename);
        }

        return new DataDto<>(new ResourceFileDto(path));
    }

    private Path getRootLocation() {
        Path path = Paths.get(rootPath);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            log.error("Cannot create directory: {}", path, e);
            throw new FileStorageException("file.invalid.path");
        }

        return path;
    }
}
