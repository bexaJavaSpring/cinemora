package bekhruz.com.cinemora.mapper;

import bekhruz.com.cinemora.dto.content.ContentRequest;
import bekhruz.com.cinemora.dto.content.ContentResponse;
import bekhruz.com.cinemora.entity.Content;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    ContentResponse toResponse(Content content);

    Content toEntity(ContentRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(ContentRequest request, @MappingTarget Content content);
}
