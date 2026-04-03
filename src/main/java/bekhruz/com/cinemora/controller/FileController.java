package bekhruz.com.cinemora.controller;

import bekhruz.com.cinemora.dto.DataDto;
import bekhruz.com.cinemora.dto.ResourceFileDto;
import bekhruz.com.cinemora.service.FileService;
import bekhruz.com.cinemora.util.ApiConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiConstants.API_VERSION + "/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DataDto<ResourceFileDto>> uploadFile(@RequestParam("file") MultipartFile file,
                                                               @RequestParam(required = false, name = "minWidth") Integer minWidth,
                                                               @RequestParam(required = false, name = "minHeight") Integer minHeight) {
        return new ResponseEntity<>(fileService.storeFile(file, minWidth, minHeight), HttpStatus.OK);
    }
}
