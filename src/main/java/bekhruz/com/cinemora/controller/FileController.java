package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.DataDto;
import bekhruz.com.cinemora.dto.ResourceFileDto;
import bekhruz.com.cinemora.service.FileService;
import bekhruz.com.cinemora.service.MinioService;
import bekhruz.com.cinemora.util.ApiConstants;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping(ApiConstants.API_VERSION + "/files")
public class FileController {

    private final FileService fileService;
    private final MinioService minioService;

    public FileController(FileService fileService, MinioService minioService) {
        this.fileService = fileService;
        this.minioService = minioService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DataDto<ResourceFileDto>> uploadFile(@RequestParam("file") MultipartFile file,
                                                               @RequestParam(required = false, name = "minWidth") Integer minWidth,
                                                               @RequestParam(required = false, name = "minHeight") Integer minHeight) {
        return new ResponseEntity<>(fileService.storeFile(file, minWidth, minHeight), HttpStatus.OK);
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<DataDto<String>> getPresignedUrl(@RequestParam String objectName) {
        String url = minioService.generatePresignedUrl(objectName);
        return ResponseEntity.ok(new DataDto<>(url));
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam String name) {
        return minioService.downloadFile(name);
    }

    @GetMapping("/view")
    public ResponseEntity<ByteArrayResource> viewFile(@RequestParam String name) {
        return minioService.viewFile(name);
    }

    @GetMapping("/video")
    public ResponseEntity<InputStreamResource> streamVideo(
            @RequestParam String name,
            @RequestHeader(value = "Range", required = false) String rangeHeader
    ) {
        return minioService.video(name, rangeHeader);
    }
}
