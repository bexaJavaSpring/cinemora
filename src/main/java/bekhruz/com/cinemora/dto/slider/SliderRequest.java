package bekhruz.com.cinemora.dto.slider;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class SliderRequest {
    @NotBlank(message = "Sarlavha kiritilishi shart")
    @Size(max = 200)
    private String title;

    @Size(max = 500)
    private String description;

    private UUID contentId;
    private String externalUrl;
    private Integer sortOrder;
    private String imageUrl;
}
