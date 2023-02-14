package com.fakelsmile.quote.service.controllers;

import com.fakelsmile.quote.service.TestUtil;
import com.fakelsmile.quote.service.models.dto.ErrorDTO;
import com.fakelsmile.quote.service.mappers.UserMapper;
import com.fakelsmile.quote.service.models.dto.user.RequestUserDTO;
import com.fakelsmile.quote.service.models.dto.user.UserShortDTO;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import com.fakelsmile.quote.service.services.UserService;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.fakelsmile.quote.service.TestUtil.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    private UserEntity userEntity;
    private UserShortDTO userShortDTO;
    private UserEntity savedUserEntity;
    private RequestUserDTO requestUserDTO;

    @BeforeEach
    public void setUp() {
        userEntity = UserEntity.builder()
                .id(3L)
                .name("Ramesh")
                .email("ramesh@gmail.com")
                .password("Fadatare")
                .build();

        requestUserDTO = new RequestUserDTO();
        requestUserDTO.setName("Ramesh");
        requestUserDTO.setEmail("ramesh@gmail.com");
        requestUserDTO.setPassword("Fadatare");

        savedUserEntity = UserEntity.builder()
                .id(3L)
                .name("Ramesh")
                .email("ramesh@gmail.com")
                .password("Fadatare")
                .build();

        userShortDTO = new UserShortDTO();
        userShortDTO.setId(3L);
        userShortDTO.setName("Ramesh");
    }

    @AfterEach
    public void turnDown() {
        verifyNoMoreInteractions(userMapper, userService);
    }

    @DisplayName("JUnit test for createUser method")
    @Test
    public void createUserTest() throws Exception {
        UserShortDTO request = new UserShortDTO();
        request.setId(3L);
        request.setName("Ramesh");

        when(userMapper.toEntity(requestUserDTO)).thenReturn(userEntity);
        when(userService.saveUser(userEntity)).thenReturn(savedUserEntity);
        when(userMapper.toDto(savedUserEntity)).thenReturn(userShortDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .content(asJsonString(requestUserDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        UserShortDTO actual = TestUtil.objectMapper.readValue(jsonResult, UserShortDTO.class);

        assertEquals(request, actual);
        verify(userMapper).toEntity(requestUserDTO);
        verify(userService).saveUser(userEntity);
        verify(userMapper).toDto(savedUserEntity);
    }

    @DisplayName("Should return the HTTP status code bad request (400) if Name is null")
    @Test
    void createUserWithNameIsNullTest() throws Exception {
        RequestUserDTO request = new RequestUserDTO();
        request.setName(null);
        request.setEmail("ramesh@gmail.com");
        request.setPassword("Fadatare");

        ErrorDTO errorDTO = new ErrorDTO("name", "Name cannot be empty");
        List<ErrorDTO> errors = List.of(errorDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ErrorDTO> actual = TestUtil.objectMapper.readValue(jsonResult, new TypeReference<>() {});
        assertEquals(errors, actual);
    }

    @DisplayName("Should return the HTTP status code bad request (400) if Email is null")
    @Test
    void createUserWithEmailIsNullTest() throws Exception {
        RequestUserDTO request = new RequestUserDTO();
        request.setName("Ramesh");
        request.setEmail(null);
        request.setPassword("Fadatare");

        ErrorDTO errorDTO = new ErrorDTO("email", "Email cannot be empty");
        List<ErrorDTO> errors = List.of(errorDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ErrorDTO> actual = TestUtil.objectMapper.readValue(jsonResult, new TypeReference<>() {});
        assertEquals(errors, actual);
    }

    @DisplayName("Should return the HTTP status code bad request (400) if Email is not valid")
    @Test
    void createUserWithEmailNotValidTest() throws Exception {
        RequestUserDTO request = new RequestUserDTO();
        request.setName("Ramesh");
        request.setEmail("rameshgmail.com");
        request.setPassword("Fadatare");

        ErrorDTO errorDTO = new ErrorDTO("email", "Email should be valid");
        List<ErrorDTO> errors = List.of(errorDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ErrorDTO> actual = TestUtil.objectMapper.readValue(jsonResult, new TypeReference<>() {});
        assertEquals(errors, actual);
    }

    @DisplayName("Should return the HTTP status code bad request (400) if Password is null")
    @Test
    void createUserWithPasswordIsNullTest() throws Exception {
        RequestUserDTO request = new RequestUserDTO();
        request.setName("Ramesh");
        request.setEmail("ramesh@gmail.com");
        request.setPassword(null);

        ErrorDTO errorDTO = new ErrorDTO("password", "Password cannot be empty");
        List<ErrorDTO> errors = List.of(errorDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ErrorDTO> actual = TestUtil.objectMapper.readValue(jsonResult, new TypeReference<>() {});
        assertEquals(errors, actual);
    }

    @DisplayName("JUnit test for getUser method")
    @Test
    public void getUserTest() throws Exception {
        when(userService.getUser(userEntity.getId())).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(userShortDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/" + userEntity.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        UserShortDTO actual = TestUtil.objectMapper.readValue(jsonResult, UserShortDTO.class);

        assertEquals(userShortDTO, actual);
        verify(userService).getUser(userEntity.getId());
        verify(userMapper).toDto(userEntity);
    }
}