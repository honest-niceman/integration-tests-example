package honest.niceman.it.example.maper;

import honest.niceman.it.example.dto.UserDto;
import honest.niceman.it.example.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto,
                       @MappingTarget User user);

    User updateWithNull(UserDto userDto,
                        @MappingTarget User user);
}