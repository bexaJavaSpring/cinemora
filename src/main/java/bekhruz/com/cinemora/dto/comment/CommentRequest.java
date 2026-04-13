package bekhruz.com.cinemora.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CommentRequest {
    @NotNull(message = "ContentId kiritilishi shart")
    private UUID contentId;

    @NotBlank(message = "Izoh matni bo'sh bo'lishi mumkin emas")
    @Size(max = 1000, message = "Izoh 1000 belgidan oshmasin")
    private String text;

    private UUID parentId;
}
