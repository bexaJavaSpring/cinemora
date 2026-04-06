package bekhruz.com.cinemora.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UploadedFileDetailsDto {
    private  String etag;
    private  String path;
}
