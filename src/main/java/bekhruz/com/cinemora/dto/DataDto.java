package bekhruz.com.cinemora.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataDto<T> {
    protected T data;
    protected boolean success;
    private Long totalCount;

    public DataDto(T data){
        this.data = data;
        this.success = true;
    }
}
