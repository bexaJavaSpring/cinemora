package bekhruz.com.cinemora.dto.slider;

import lombok.Data;

import java.util.UUID;
@Data
public class SliderResponse {
    private UUID id;
    private String  title;
    private String  description;
    private String  imageUrl;
    private String  externalUrl;
    private Boolean isActive;
    private Integer sortOrder;
    private UUID    contentId;
    private String  contentSlug;
    private String  contentTitle;
}
