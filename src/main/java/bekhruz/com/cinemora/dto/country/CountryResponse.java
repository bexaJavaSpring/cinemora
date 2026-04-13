package bekhruz.com.cinemora.dto.country;

import lombok.Data;

import java.util.UUID;

@Data
public class CountryResponse {
    private UUID id;
    private String  name;
    private String  code;
}
