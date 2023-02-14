package com.fakelsmile.quote.service.mappers;

import com.fakelsmile.quote.service.models.dto.user.RequestUserDTO;
import com.fakelsmile.quote.service.models.dto.user.UserShortDTO;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    private UserMapperImpl userMapper;
    private RequestUserDTO requestUserDTO;


    @BeforeEach
    public void setUp() {
        requestUserDTO = new RequestUserDTO();
        requestUserDTO.setName("Zaph");
        requestUserDTO.setEmail("Zaph@gmail");
        requestUserDTO.setPassword("qwerty123");
    }

    @DisplayName("JUnit test for map userEntity to userDTO")
    @Test
    void toDTOFromUserEntityTest() {
        UserEntity userEntityNullPassword = UserEntity.builder()
                .id(1L)
                .name("Zaph")
                .email("Zaph@gmail")
                .password(null)
                .build();

        UserShortDTO expected = new UserShortDTO();
        expected.setId(userEntityNullPassword.getId());
        expected.setName(userEntityNullPassword.getName());

        UserShortDTO result = userMapper.toDto(userEntityNullPassword);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @DisplayName("JUnit test for map userDTO to userEntity")
    @Test
    void toEntityFromUserDTOTest() {
        UserEntity expected = new UserEntity();
        expected.setName(requestUserDTO.getName());
        expected.setEmail(requestUserDTO.getEmail());
        expected.setPassword(requestUserDTO.getPassword());

        UserEntity actual = userMapper.toEntity(requestUserDTO);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}