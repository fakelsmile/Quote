package com.fakelsmile.quote.service.mappers;

import com.fakelsmile.quote.service.models.dto.user.RequestUserDTO;
import com.fakelsmile.quote.service.models.dto.user.UserShortDTO;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for users.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Map UserDTO to UserEntity.
     *
     * @param requestUserDTO - user DTO
     * @return mapped userEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password")
    UserEntity toEntity(RequestUserDTO requestUserDTO);

    /**
     * Map UserEntity to UserShortDTO.
     *
     * @param userEntity - user entity
     * @return mapped UserShortDTO
     */
    UserShortDTO toDto(UserEntity userEntity);
}
